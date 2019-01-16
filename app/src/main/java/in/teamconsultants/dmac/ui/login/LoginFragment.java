package in.teamconsultants.dmac.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.LoginResponse;
import in.teamconsultants.dmac.network.ApiClient;
import in.teamconsultants.dmac.network.ApiInterface;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerHomeActivity;
import in.teamconsultants.dmac.ui.home.dashboard.fe.FeHomeActivity;
import in.teamconsultants.dmac.ui.registration.RegisterActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText etUsername, etPassword;
    private LinearLayout btnCreateAccount, btnLogin;
    private CheckBox cbRememberMe;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = v.findViewById(R.id.et_username);
        etPassword = v.findViewById(R.id.et_password);
        btnLogin = v.findViewById(R.id.grp_login);
        btnCreateAccount = v.findViewById(R.id.grp_create_account);
        cbRememberMe = v.findViewById(R.id.cb_remember_me);


        etUsername.setText("fe@dmac.com");
        etPassword.setText("9848012345");

        setOnClickListeners();
        return v;
    }

    private void setOnClickListeners() {

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationAlert();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String username = etUsername.getText().toString();
                if(username.contains("customer")) {
                    startActivity(new Intent(getActivity(), CustomerHomeActivity.class));
                    getActivity().finish();
                }
                else if (username.contains("fe")) {
                    startActivity(new Intent(getActivity(), FeHomeActivity.class));
                    getActivity().finish();
                }
*/              String email = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
                }
                else {
                    validateUser(email, password);
                }

            }
        });

    }

    private void validateUser(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Validating your credentials...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<LoginResponse> apiCall = apiInterface.doUserLogin(email, password);

        final Gson gson = new Gson();

        apiCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                LoginResponse loginResponse = response.body();

                if(loginResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    SharedPreferences spUserData = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);
                    SharedPreferences.Editor userDataEditor = spUserData.edit();
                    userDataEditor.putString(AppConstants.SP.TAG_USER_DETAILS, gson.toJson(loginResponse.getUserData()));
                    userDataEditor.putString(AppConstants.SP.TAG_TOKEN, loginResponse.getTokenId());
                    userDataEditor.commit();
                    if(loginResponse.getUserData().getRoleId().equals(AppConstants.USER_ROLE.CUSTOMER)){
                        startActivity(new Intent(getActivity(), CustomerHomeActivity.class));
                        getActivity().finish();
                    }
                    else if(loginResponse.getUserData().getRoleId().equals(AppConstants.USER_ROLE.FE)){
                        startActivity(new Intent(getActivity(), FeHomeActivity.class));
                        getActivity().finish();
                    }

                }
                else if (loginResponse.getStatus().equals(AppConstants.RESPONSE.FAILED)){
                    Utility.showAlert(getContext(), "Invalid credentials", "Please verify your email and password and try again");
                }
                else {
                    Utility.showAlert(getContext(), "Error", "An unknown error occurred, please try again");
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlert(getContext(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
                Log.d(AppConstants.LOG_TAG, "Call: "+call.request().toString());
                Log.d(AppConstants.LOG_TAG, "Body: "+gson.toJson(call.request().body()));
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showRegistrationAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_create_account, null);
        builder.setView(dialogLayout);

        final AlertDialog dialog = builder.create();

        LinearLayout grpQuickReg = dialogLayout.findViewById(R.id.grp_quick_registration);
        LinearLayout grpRegularReg = dialogLayout.findViewById(R.id.grp_regular_registration);

        grpQuickReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.QUICK);
                intent.putExtra(AppConstants.INTENT_TAG.REG_INITIATOR, AppConstants.REGISTRATION.INITIATOR.CUSTOMER);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        grpRegularReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.REGULAR);
                intent.putExtra(AppConstants.INTENT_TAG.REG_INITIATOR, AppConstants.REGISTRATION.INITIATOR.CUSTOMER);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
