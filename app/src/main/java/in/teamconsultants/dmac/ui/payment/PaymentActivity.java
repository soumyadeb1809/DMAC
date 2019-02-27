package in.teamconsultants.dmac.ui.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.UpdateInvoicePaymentResponse;
import in.teamconsultants.dmac.ui.home.invoices.InvoicesActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private String token;

    private String invoiceName;
    private String invoiceAmount;
    private String currency;
    private String invoiceId;

    private Toolbar toolbar;
    private LinearLayout grpStatus, grpAction;
    private TextView tvStatus, tvAction, tvInvoiceName, tvInvoiceAmount, tvInvoiceTitle, tvAmountTitle;
    private ImageView imgStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progress = new ProgressDialog(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        invoiceName = intent.getStringExtra(AppConstants.INTENT_TAG.INVOICE_NAME);
        invoiceAmount = intent.getStringExtra(AppConstants.INTENT_TAG.INVOICE_AMOUNT);
        invoiceId = intent.getStringExtra(AppConstants.INTENT_TAG.INVOICE_ID);
        currency = "INR";

        initializeUi();

        startPayment();

    }

    private void initializeUi() {

        tvStatus = findViewById(R.id.txt_payment_status);
        tvAction = findViewById(R.id.txt_action);
        grpStatus = findViewById(R.id.grp_status);
        imgStatus = findViewById(R.id.img_status);
        grpAction = findViewById(R.id.grp_action);
        tvInvoiceName = findViewById(R.id.txt_invoice_name);
        tvInvoiceAmount = findViewById(R.id.txt_invoice_amount);
        tvAmountTitle = findViewById(R.id.title_invoice_paid);
        tvInvoiceTitle = findViewById(R.id.title_invoice_name);

        grpAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        grpStatus.setVisibility(View.GONE);
        grpAction.setVisibility(View.GONE);

    }


    /**
     * Method to initiate payment via RazorPay
     */
    public void startPayment() {

        progress.setMessage("Processing your payment...");
        progress.setCancelable(false);
        progress.show();

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set company logo here
         */
        checkout.setImage(R.drawable.dmac_logo_small);

        /**
         * Reference to current activity
         */
        final Activity activity = PaymentActivity.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "DMAC");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", invoiceName);
            options.put("currency", currency);

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            double invoiceAmountDbl = (double) Math.round(Double.parseDouble(invoiceAmount) * 100d) / 100d;
            Log.d(AppConstants.LOG_TAG, "Invoice amount in Rs:: " + String.valueOf(invoiceAmountDbl));
            double amountInPaise = invoiceAmountDbl * 100;
            Log.d(AppConstants.LOG_TAG, "Invoice amount in Paise:: " + String.valueOf(amountInPaise));

            options.put("amount", String.valueOf(amountInPaise));

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(AppConstants.LOG_TAG, "Error in starting RazorPay Checkout", e);
        }
    }


    /**
     * RazorPay payment callbacks that are called after every successful or failed payments
     */
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        //Utility.showAlert(this, "Payment Success: " + razorpayPaymentID, "Payment for invoice was successful" );
        updateDataInDB(razorpayPaymentID);
        Log.d(AppConstants.LOG_TAG, "Payment success, RazorPay payment ID: " + razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int errorCode, String response) {
        progress.dismiss();
        //Utility.showAlert(this, "Payment Failed", "Payment failed, reason: " + response );

        tvStatus.setText("Payment Failed");
        tvAction.setText("Go Back");
        tvInvoiceName.setText("Reason: " + response);
        imgStatus.setImageResource(R.drawable.ic_failed);
        tvAmountTitle.setVisibility(View.GONE);
        tvInvoiceTitle.setVisibility(View.GONE);
        tvInvoiceAmount.setVisibility(View.GONE);
        grpAction.setVisibility(View.VISIBLE);
        grpStatus.setVisibility(View.VISIBLE);
        Log.d(AppConstants.LOG_TAG, "RazorPay error code: " + errorCode);
        Log.d(AppConstants.LOG_TAG, "RazorPay error response: " + response);
    }


    /**
     * Method to updated the payment in the DB
     * razorpayPayment ID and invoice ID are sent via update_invoice_payment API
     *
     * @param razorpayPaymentID
     */
    private void updateDataInDB(final String razorpayPaymentID) {

        Call<UpdateInvoicePaymentResponse> updateInvoicePaymentCall = apiInterface.doUpdateInvoicePayment(token, razorpayPaymentID, invoiceId);
        updateInvoicePaymentCall.enqueue(new Callback<UpdateInvoicePaymentResponse>() {
            @Override
            public void onResponse(Call<UpdateInvoicePaymentResponse> call, Response<UpdateInvoicePaymentResponse> response) {
                progress.dismiss();
                UpdateInvoicePaymentResponse updateInvoicePaymentResponse = response.body();
                if(null != updateInvoicePaymentResponse.getStatus() && updateInvoicePaymentResponse.getStatus().equals(AppConstants.RESPONSE.FAILED)){
                    /*Utility.showAlert(PaymentActivity.this, "Error",
                            "Something went wrong while saving your data. Please contact DMAC support and share your payment ID: " + razorpayPaymentID);*/


                    tvStatus.setText("Payment Failed");
                    tvAction.setText("Go Back");
                    tvInvoiceName.setText("Reason: " + "Failed to save payment details.\nPlease contact DMAC support and share your payment ID: " + razorpayPaymentID );
                    imgStatus.setImageResource(R.drawable.ic_failed);
                    tvAmountTitle.setVisibility(View.GONE);
                    tvInvoiceTitle.setVisibility(View.GONE);
                    tvInvoiceAmount.setVisibility(View.GONE);
                    grpAction.setVisibility(View.VISIBLE);
                    grpStatus.setVisibility(View.VISIBLE);

                }
                else {
                    //Utility.showAlert(PaymentActivity.this, "Payment Success: " + razorpayPaymentID, "Payment for invoice " + invoiceName + " was successful" );
                    tvStatus.setText("Payment Successful");
                    tvAction.setText("Finish");
                    tvInvoiceAmount.setText("₹" +invoiceAmount);
                    tvInvoiceName.setText(invoiceName);
                    tvAmountTitle.setVisibility(View.VISIBLE);
                    tvInvoiceTitle.setVisibility(View.VISIBLE);
                    imgStatus.setImageResource(R.drawable.ic_checked);
                    grpAction.setVisibility(View.VISIBLE);
                    grpStatus.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UpdateInvoicePaymentResponse> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                /*Utility.showAlert(PaymentActivity.this, "Error",
                        "Something went wrong while saving your data. Please contact DMAC support and share your payment ID: " + razorpayPaymentID);*/

                tvStatus.setText("Payment Failed");
                tvAction.setText("Go  Back");
                tvInvoiceName.setText("Reason: " + "Failed to save payment details.\nPlease contact DMAC support and share your payment ID: " + razorpayPaymentID );
                imgStatus.setImageResource(R.drawable.ic_failed);
                tvAmountTitle.setVisibility(View.GONE);
                tvInvoiceTitle.setVisibility(View.GONE);
                tvInvoiceAmount.setVisibility(View.GONE);
                grpAction.setVisibility(View.VISIBLE);
                grpStatus.setVisibility(View.VISIBLE);
            }
        });

    }

}
