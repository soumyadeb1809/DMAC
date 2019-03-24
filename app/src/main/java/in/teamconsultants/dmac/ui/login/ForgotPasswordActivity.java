 package in.teamconsultants.dmac.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.ForgotPasswordResponse;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import in.teamconsultants.dmac.utils.ValidationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

 public class ForgotPasswordActivity extends AppCompatActivity {

    private LinearLayout grpResetPassword, grpBack;
    private EditText etEmail;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        apiInterface  = ApiClient.getApiClient().create(ApiInterface.class);

        initializeUi();
        setOnClickListeners();

    }

     private void setOnClickListeners() {

        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        grpResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserInput();
            }
        });

     }

     private void initializeUi() {
        grpResetPassword = findViewById(R.id.grp_reset_password);
        etEmail = findViewById(R.id.et_email);
        grpBack = findViewById(R.id.grp_back);

        progressDialog = new ProgressDialog(this);
     }


     private void validateUserInput() {
        String email = etEmail.getText().toString();

        if(!ValidationUtils.isValidEmail(email)){
            Utility.showAlert(this, "Info", "Please enter a valid email address.");
            return;
        }

        resetUserPassword(email);

     }

     private void resetUserPassword(String email) {

         progressDialog.setMessage("Sending password reset link...");
         progressDialog.setCancelable(false);
         progressDialog.show();

         Call<ForgotPasswordResponse> forgotPasswordCall = apiInterface.doForgotPassword(email);

         forgotPasswordCall.enqueue(new Callback<ForgotPasswordResponse>() {
             @Override
             public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {

                 progressDialog.dismiss();

                 if(response.body() == null)
                     return;

                 ForgotPasswordResponse forgotPasswordResponse = response.body();

                 AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                 alertBuilder.setCancelable(false);
                 alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         finish();
                     }
                 });

                 if(forgotPasswordResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                     alertBuilder.setTitle("Success");
                     alertBuilder.setMessage("Verification link successfully sent your email. Please check you email for further instructions.");
                 }
                 else {
                     alertBuilder.setTitle("Info");
                     alertBuilder.setMessage("Verification link already sent your email. Please check you email for further instructions.");
                 }

                 alertBuilder.show();

             }

             @Override
             public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                 progressDialog.dismiss();
                 t.printStackTrace();
                 Utility.showAlert(ForgotPasswordActivity.this, "Error", "Something went wrong, please try again.");
             }
         });

     }
 }
