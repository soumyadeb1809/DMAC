package in.teamconsultants.dmac.ui.home.profile;

import androidx.appcompat.app.AppCompatActivity;
import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.EditProfileResponse;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerHomeActivity;
import in.teamconsultants.dmac.ui.home.dashboard.fe.FeHomeActivity;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

public class EditProfileActivity extends AppCompatActivity {

    private LinearLayout grpBack, grpSaveChanges, grpConfirmPassword;
    private EditText etFullName, etPhone, etPassword, etConfirmPassword;
    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Intent intent = getIntent();

        initializeUi();

        prePopulateFields(intent);

        setEventListeners();

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(this);


    }

    private void initializeUi() {

        grpBack = findViewById(R.id.grp_back);
        etFullName = findViewById(R.id.et_full_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        grpConfirmPassword = findViewById(R.id.grp_confirm_password);
        grpSaveChanges = findViewById(R.id.grp_save_changes);

    }

    private void prePopulateFields(Intent intent) {
        if(intent == null)
            return;

        String fullName = intent.getStringExtra(AppConstants.INTENT_TAG.USER_FULL_NAME);
        String phone = intent.getStringExtra(AppConstants.INTENT_TAG.USER_PHONE);

        etFullName.setText(fullName);
        etPhone.setText(phone);
    }


    private void setEventListeners() {

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    grpConfirmPassword.setVisibility(View.INVISIBLE);
                }
                else {
                    grpConfirmPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        grpSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserInput();
            }
        });

    }

    private void validateUserInput() {

        String fullName = etFullName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone)){
            Utility.showAlert(this, "Info", "Name and Phone cannot be left empty. Please try again.");
            return;
        }

        if(phone.length() < 10){
            Utility.showAlert(this, "Info", "Please enter a valid phone number and try again.");
            return;
        }

        if(!TextUtils.isEmpty(password) && !password.equals(confirmPassword)){
            Utility.showAlert(this, "Info", "Passwords do not match. PLease try again.");
            return;
        }

        updateUserProfile(fullName, phone, password);

    }

    private void updateUserProfile(String fullName, String phone, String password) {

        progress.setMessage("Updating profile info...");
        progress.setCancelable(false);
        progress.show();

        Call<EditProfileResponse> editProfileCall = apiInterface.doUpdateProfile(token, fullName, phone, password);
        editProfileCall.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

                EditProfileResponse editProfileResponse = response.body();

                if(editProfileResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    final UserData userData = editProfileResponse.getUserData();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AppConstants.SP.TAG_USER_DETAILS, gson.toJson(userData));
                    editor.commit();

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                    alertBuilder.setTitle("Success");
                    alertBuilder.setMessage("Your profile has been updated successfully.");
                    alertBuilder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String userRole = userData.getRoleId();

                            Intent intent = null;

                            if(userRole.equals(AppConstants.USER_ROLE.FE)){
                                intent = new Intent(EditProfileActivity.this, FeHomeActivity.class);
                            }
                            else if(userRole.equals(AppConstants.USER_ROLE.CUSTOMER)) {
                                intent = new Intent(EditProfileActivity.this, CustomerHomeActivity.class);
                            }

                            if(intent != null) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            finish();

                        }
                    });
                    alertBuilder.setCancelable(false);
                    alertBuilder.show();

                }
                else {
                    Utility.forceLogoutUser(EditProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(EditProfileActivity.this, "Error", "Something went wrong, please try again");
            }
        });

    }

}
