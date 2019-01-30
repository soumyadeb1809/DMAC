package in.teamconsultants.dmac.ui.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FileTypeResponse;
import in.teamconsultants.dmac.model.QuickRegisterResponse;
import in.teamconsultants.dmac.network.ApiClient;
import in.teamconsultants.dmac.network.ApiInterface;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickRegisterFragment extends Fragment {

    private OnQuickRegisterFragmentInteractionListener mListener;

    private EditText etBusinessLegalName, etBusinessShortName, etBusinessAddress, etPinCode;
    private EditText etFullName, etEmail, etPhone, etPassword;
    private Spinner spinnerState, spinnerCity, spinnerBusinessEntityType;
    private LinearLayout btnCreateAccount;
    private CheckBox cbTc;


    private String[] states = {"Andrapradesh", "Telangana"};

    private String[] cities = {"Hyderabad", "Nalgonda", "Karimnagar"};

    private String[] entities = {"Proprietary", "Partnership Firm", "LLP", "Company", "Society", "Trust"};

    private SimpleSpinnerAdapter statesSpinnerAdapter;
    private SimpleSpinnerAdapter citySpinnerAdapter;
    private SimpleSpinnerAdapter entityAdapter;

    private ApiInterface apiInterface;
    private Gson gson;

    private ProgressDialog progress;

    public QuickRegisterFragment() {
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
        View v = inflater.inflate(R.layout.fragment_quick_register, container, false);

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progress = new ProgressDialog(getContext());

        gson = new Gson();

        initializeUi(v);

        setOnClickListeners();

        return v;
    }


    private void initializeUi(View v) {
        etBusinessLegalName = v.findViewById(R.id.et_business_legal_name);
        etBusinessShortName = v.findViewById(R.id.et_business_short_name);
        etBusinessAddress = v.findViewById(R.id.et_business_address);
        etPinCode = v.findViewById(R.id.et_pin_code);

        etFullName = v.findViewById(R.id.et_full_name);
        etEmail = v.findViewById(R.id.et_email);
        etPhone = v.findViewById(R.id.et_phone);
        etPassword = v.findViewById(R.id.et_password);

        spinnerState = v.findViewById(R.id.spinner_state);
        spinnerCity = v.findViewById(R.id.spinner_city);
        spinnerBusinessEntityType = v.findViewById(R.id.spinner_entity_type);

        btnCreateAccount = v.findViewById(R.id.grp_create_account);
        cbTc = v.findViewById(R.id.cb_tc);

        statesSpinnerAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, states);
        citySpinnerAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, cities);
        entityAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, entities);

        spinnerState.setAdapter(statesSpinnerAdapter);
        spinnerCity.setAdapter(citySpinnerAdapter);
        spinnerBusinessEntityType.setAdapter(entityAdapter);

    }

    private void setOnClickListeners() {

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm(v);
            }
        });

    }

    private void validateForm(View v) {

        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        String businessLegalName = etBusinessLegalName.getText().toString();
        String businessShortName = etBusinessShortName.getText().toString();
        String businessAddress = etBusinessAddress.getText().toString();
        String pinCode = etPinCode.getText().toString();

        if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)
                ||  TextUtils.isEmpty(businessLegalName) || TextUtils.isEmpty(businessShortName) || TextUtils.isEmpty(businessAddress)
                || TextUtils.isEmpty(pinCode)) {
            Utility.showAlert(getContext(), "Info", "All fields are mandatory. Please make sure you've filled them all");
        }
        else if(!cbTc.isChecked()){
            Utility.showAlert(getContext(), "Info", "Please agree to DMAC T&C to continue");
        }
        else if(phone.length() < 10){
            Utility.showAlert(getContext(), "Info", "Please enter an valid phone number");
        }
        else if(password.length() < 6) {
            Utility.showAlert(getContext(), "Info", "Password must be at least 6 characters long");
        }
        else {
            quickRegister(fullName, email, phone, password,businessLegalName, businessShortName, businessAddress, pinCode);
        }

    }

    private void quickRegister(String fullName, String email, String phone, String password,
                               String businessLegalName, String businessShortName, String businessAddress, String pinCode) {

        progress.setMessage("Creating account...");
        progress.setCancelable(false);
        progress.show();

        String state = (String) spinnerState.getSelectedItem();
        String city = (String) spinnerCity.getSelectedItem();
        String entityType = (String) spinnerBusinessEntityType.getSelectedItem();

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("FullName", fullName);
        fieldMap.put("Email", email);
        fieldMap.put("Phone", phone);
        fieldMap.put("Password", password);
        fieldMap.put("BusinessLegalName", businessLegalName);
        fieldMap.put("BusinessShortName", businessShortName);
        fieldMap.put("BusinessAddress", businessAddress);
        fieldMap.put("CityCode", city);
        fieldMap.put("StateCode", state);
        fieldMap.put("PinCode", pinCode);
        fieldMap.put("TypeOfEntity", entityType);

        Call<QuickRegisterResponse> quickRegisterResponseCall = apiInterface.doQuickRegistration(fieldMap);
        quickRegisterResponseCall.enqueue(new Callback<QuickRegisterResponse>() {
            @Override
            public void onResponse(Call<QuickRegisterResponse> call, Response<QuickRegisterResponse> response) {
                progress.dismiss();
                //Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(response.body()));
                QuickRegisterResponse quickRegisterResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(quickRegisterResponse));

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

                if(quickRegisterResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    alertBuilder.setTitle("Success");
                    alertBuilder.setMessage("Account created successfully");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
                    alertBuilder.show();
                }
                else {
                    if(quickRegisterResponse.getDetails().getEmail() != null){
                        Utility.showAlert(getContext(), "Info", "Email is already registered, please try again");
                    }
                    else {
                        Utility.showAlert(getContext(), "Error", "Something went wrong, please try again");
                    }
                }

            }

            @Override
            public void onFailure(Call<QuickRegisterResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                Utility.showAlert(getContext(), "Error", "Something went wrong, please try again");
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuickRegisterFragmentInteractionListener) {
            mListener = (OnQuickRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnQuickRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnQuickRegisterFragmentInteractionListener {
        void onQuickRegisterFragmentInteraction(Uri uri);
    }
}
