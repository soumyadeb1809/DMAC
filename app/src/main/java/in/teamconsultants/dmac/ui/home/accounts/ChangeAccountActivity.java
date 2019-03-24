package in.teamconsultants.dmac.ui.home.accounts;

import androidx.appcompat.app.AppCompatActivity;
import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerAccount;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.CustomerAccountsResponse;
import in.teamconsultants.dmac.network.dto.SwitchAccountResponse;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerHomeActivity;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.razorpay.Checkout;

import java.util.List;

public class ChangeAccountActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private SharedPreferences sp;

    private List<CustomerAccount> customerAccountList;
    private String[] customerAccountsArr;

    private String token;

    private Spinner spinCustomerAccount;
    private LinearLayout grpChangeAccount, grpBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Checkout.preload(getApplicationContext());

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(this);

        sp = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);

        initializeUi();

        CustomerAccountsResponse customerAccountsResponse = gson.fromJson(
                sp.getString(AppConstants.SP.TAG_USER_ACCOUNT_LIST, null), CustomerAccountsResponse.class);

        if(customerAccountsResponse == null){
            fetchCustomerAccounts();
        }
        else {
            customerAccountList = customerAccountsResponse.getAccountList();
            prepareAccountArrFromList();
            setUpSpinner();
        }

    }

    private void prepareAccountArrFromList() {

        customerAccountsArr = new String[customerAccountList.size()];

        for(int i = 0; i < customerAccountList.size(); i++){
            customerAccountsArr[i] = customerAccountList.get(i).getBusinessLegalName();
        }

    }

    private void initializeUi() {

        spinCustomerAccount = findViewById(R.id.spinner_company_list);
        grpChangeAccount = findViewById(R.id.grp_change_company);
        grpBack = findViewById(R.id.grp_back);

        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        grpChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerAccountList != null || customerAccountList.size() != 0){
                    switchCustomerCompany();
                }
            }
        });


    }

    private void setUpSpinner() {

        SimpleSpinnerAdapter spinnerAdapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout_large, customerAccountsArr);
        spinCustomerAccount.setAdapter(spinnerAdapter);

        UserData userData = gson.fromJson(sp.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);

        if(userData == null){
            return;
        }

        String accountId = userData.getLogedInAccount();

        int position = searchForAccount(accountId);

        Log.d(AppConstants.LOG_TAG, "Company AId: " + userData.getLogedInAccount());
        Log.d(AppConstants.LOG_TAG, "Company pos: " + position);

        if(position != -1){
            spinCustomerAccount.setSelection(position);
        }

    }

    private int searchForAccount(String accountId) {

        for(int i = 0; i < customerAccountList.size(); i++){
            if(customerAccountList.get(i).getAId().equals(accountId)){
                return i;
            }
        }
        return -1;

    }


    private void fetchCustomerAccounts() {

        progress.setMessage("Fetching your accounts...");
        progress.setCancelable(false);
        progress.show();

        Call<CustomerAccountsResponse> customerAccountsCall = apiInterface.doGetCustomerAccountList(token);

        customerAccountsCall.enqueue(new Callback<CustomerAccountsResponse>() {
            @Override
            public void onResponse(Call<CustomerAccountsResponse> call, Response<CustomerAccountsResponse> response) {
                    progress.dismiss();

                    if(response.body() == null)
                        return;

                    CustomerAccountsResponse customerAccountsResponse = response.body();

                    if (customerAccountsResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {

                        customerAccountList = customerAccountsResponse.getAccountList();

                        prepareAccountArrFromList();
                        setUpSpinner();

                    } else {
                        Utility.forceLogoutUser(ChangeAccountActivity.this);
                    }

                }

                @Override
                public void onFailure(Call<CustomerAccountsResponse> call, Throwable t) {
                    progress.dismiss();
                    t.printStackTrace();
                    Utility.showAlert(ChangeAccountActivity.this, "Info", "Something went wrong, please try again");
                }
            });
        }



    private void switchCustomerCompany() {

        progress.setMessage("Changing your company...");
        progress.setCancelable(false);
        progress.show();

        int selectedItemPos = spinCustomerAccount.getSelectedItemPosition();

        final CustomerAccount selectedAccount = customerAccountList.get(selectedItemPos);

        String selectedAccountId = selectedAccount.getAId();

        Call<SwitchAccountResponse> switchAccountCall = apiInterface.doSwitchCustomerAccount(token, selectedAccountId);

        switchAccountCall.enqueue(new Callback<SwitchAccountResponse>() {
            @Override
            public void onResponse(Call<SwitchAccountResponse> call, Response<SwitchAccountResponse> response) {
                progress.dismiss();
                SwitchAccountResponse switchAccountResponse = response.body();

                if(switchAccountResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){

                    UserData userData = switchAccountResponse.getUserData();
                    String token = switchAccountResponse.getTokenId();

                    SharedPreferences.Editor userDataEditor = sp.edit();
                    userDataEditor.putString(AppConstants.SP.TAG_USER_DETAILS, gson.toJson(userData));
                    userDataEditor.putBoolean(AppConstants.SP.IS_USER_LOGGED_IN, true);
                    userDataEditor.putString(AppConstants.SP.TAG_TOKEN, token);
                    userDataEditor.commit();

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ChangeAccountActivity.this);
                    alertBuilder.setTitle("Success");
                    alertBuilder.setMessage("You have successfully changed your company, now all the data will be shown for " + selectedAccount.getBusinessLegalName());
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ChangeAccountActivity.this, CustomerHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                    alertBuilder.show();

                }
                else {
                    Log.d(AppConstants.LOG_TAG, "ChangeAccountActivity:: Token expired");
                    Utility.forceLogoutUser(ChangeAccountActivity.this);
                }

            }

            @Override
            public void onFailure(Call<SwitchAccountResponse> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Utility.showAlert(ChangeAccountActivity.this, "Info", "Something went wrong, please try again");
            }
        });


    }


}
