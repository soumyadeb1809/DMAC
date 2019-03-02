package in.teamconsultants.dmac.ui.home.invoices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Invoice;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.InvoiceResponse;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.ui.payment.PaymentActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoicesActivity extends AppCompatActivity implements InvoiceAdapter.OnInvoiceSelectedListener {

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private String token;

    private List<Invoice> invoiceList;
    private List<Invoice> invoiceListToShow;
    private InvoiceAdapter invoiceAdapter;
    private RecyclerView rvInvoices;

    private LinearLayout grpBack;
    private LinearLayout grpPayNow;

    private SwipeRefreshLayout swipeRefresh;

    private Invoice selectedInvoice = null;

    private String[] paymentStatusArr = {"All Invoices", "Paid Invoices"};
    private Spinner spinInvoiceSataus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Checkout.preload(getApplicationContext());

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(this);

        initializeUi();

        fetchInvoices();

    }

    private void fetchInvoices() {

        progress.setMessage("Fetching your invoices...");
        progress.setCancelable(false);
        progress.show();

        Call<InvoiceResponse> invoiceListCall = apiInterface.doGetInvoiceList(token, "");
        invoiceListCall.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                progress.dismiss();
                InvoiceResponse invoiceResponse = response.body();

                if(invoiceResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    invoiceList = invoiceResponse.getInvoiceList();

                    prepareInvoiceListToShow();

                }
                else {
                    Utility.showAlert(InvoicesActivity.this, "Info", "Something went wrong, please try again");
                    Log.d(AppConstants.LOG_TAG, "InvoiceResponse:: " + gson.toJson(invoiceResponse));
                }

            }

            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Utility.showAlert(InvoicesActivity.this, "Info", "Something went wrong, please try again");
            }
        });

    }

    private void prepareInvoiceListToShow() {

        invoiceListToShow = new ArrayList<>();

        int selectedStatusPos = spinInvoiceSataus.getSelectedItemPosition();

        for(Invoice invoice : invoiceList){
            if(selectedStatusPos == 1) {
                if (invoice.getStatus().equals("Paid")){
                    invoiceListToShow.add(invoice);
                }
            }
            else {
                invoiceListToShow.add(invoice);
            }
        }

        reloadRecyclerView();

    }

    private void initializeUi() {

        rvInvoices = findViewById(R.id.rv_invoices);
        rvInvoices.setLayoutManager(new LinearLayoutManager(this));
        rvInvoices.setNestedScrollingEnabled(false);


        spinInvoiceSataus = findViewById(R.id.spinner_invoice_type);
        SimpleSpinnerAdapter spinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout, paymentStatusArr);
        spinInvoiceSataus.setAdapter(spinnerAdapter);

        spinInvoiceSataus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(invoiceListToShow != null){
                    prepareInvoiceListToShow();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        grpBack = findViewById(R.id.grp_back);
        grpPayNow = findViewById(R.id.grp_pay_now);

        if(selectedInvoice == null){
            grpPayNow.setVisibility(View.GONE);
        }

        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        grpPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedInvoice == null){
                    Utility.showAlert(InvoicesActivity.this, "Info", "No invoice selected. Please select a invoice to continue");
                }
                else {
                    startPayment();
                }
            }
        });

        swipeRefresh = findViewById(R.id.swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                fetchInvoices();
            }
        });

    }

    private void startPayment() {

        String invoiceName = selectedInvoice.getInvoiceName();
        String invoiceAmount = selectedInvoice.getTotalAmount();
        String invoiceId = selectedInvoice.getIId();

        if(invoiceAmount == null || invoiceName == null || invoiceId == null || TextUtils.isEmpty(invoiceAmount)
                || TextUtils.isEmpty(invoiceName) || TextUtils.isEmpty(invoiceId)){
            Utility.showAlert(this, "Info", "Selected invoice doest not have valid information, please contact DMAC support");
        }
        else {
            Intent intent = new Intent(InvoicesActivity.this, PaymentActivity.class);
            intent.putExtra(AppConstants.INTENT_TAG.INVOICE_NAME, invoiceName);
            intent.putExtra(AppConstants.INTENT_TAG.INVOICE_AMOUNT, invoiceAmount);
            intent.putExtra(AppConstants.INTENT_TAG.INVOICE_ID, invoiceId);
            startActivity(intent);
        }

    }


    private void reloadRecyclerView() {

        invoiceAdapter = new InvoiceAdapter(InvoicesActivity.this, invoiceListToShow);
        rvInvoices.setAdapter(invoiceAdapter);
        selectedInvoice = null;
        updatePayButtonVisibility(false);

    }



    @Override
    public void updateSelectedInvoiceItem(Invoice invoice) {
        this.selectedInvoice = invoice;
    }

    @Override
    public void updatePayButtonVisibility(boolean showPayBtn) {
        if(showPayBtn && selectedInvoice!= null){
            grpPayNow.setVisibility(View.VISIBLE);
        }
        else {
            grpPayNow.setVisibility(View.GONE);
        }
    }


    /**
     * Fetch all the invoices again every time the activity restarts
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        fetchInvoices();
    }
}
