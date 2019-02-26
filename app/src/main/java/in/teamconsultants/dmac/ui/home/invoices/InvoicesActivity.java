package in.teamconsultants.dmac.ui.home.invoices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Invoice;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.InvoiceResponse;
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
    private InvoiceAdapter invoiceAdapter;
    private RecyclerView rvInvoices;

    private LinearLayout grpBack;
    private LinearLayout grpPayNow;

    private Invoice selectedInvoice = null;

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


        fetchInvoices();

    }

    private void fetchInvoices() {

        progress.setMessage("Fetching your invoices...");
        progress.setCancelable(false);
        progress.show();

        Call<InvoiceResponse> invoiceListCall = apiInterface.doGetInvoiceList(token, "21");
        invoiceListCall.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse> response) {
                progress.dismiss();
                InvoiceResponse invoiceResponse = response.body();

                if(invoiceResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    invoiceList = invoiceResponse.getInvoiceList();
                    initializeUi();

                    reloadRecyclerView();
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

    private void initializeUi() {

        rvInvoices = findViewById(R.id.rv_invoices);
        rvInvoices.setLayoutManager(new LinearLayoutManager(this));

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

        invoiceAdapter = new InvoiceAdapter(InvoicesActivity.this, invoiceList);
        rvInvoices.setAdapter(invoiceAdapter);

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
