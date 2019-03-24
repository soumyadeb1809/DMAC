package in.teamconsultants.dmac.ui.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.City;
import in.teamconsultants.dmac.model.State;
import in.teamconsultants.dmac.network.dto.CityResponse;
import in.teamconsultants.dmac.network.dto.QuickRegisterResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.StateResponse;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import in.teamconsultants.dmac.utils.ValidationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickRegisterFragment extends Fragment {

    private OnQuickRegisterFragmentInteractionListener mListener;

    private EditText etBusinessLegalName, etBusinessShortName, etBusinessAddress, etPinCode;
    private EditText etFullName, etEmail, etPhone, etPassword;
    private Spinner stateSpinner, citySpinner, spinnerBusinessEntityType;
    private LinearLayout btnCreateAccount;
    private CheckBox cbTc;


    private String[] stateArr;
    private String[] citieArr;
    private List<State> stateList;
    private List<City> cityList;
    private Map<String, String[]> citiesMap;
    private Map<String, String> stateCodeMap;
    private Map<String, String> cityCodeMap;
    private String selectedState = null;

    private String[] entities = {"Proprietary", "Partnership Firm", "LLP", "Company", "Society", "Trust"};

    private SimpleSpinnerAdapter statesSpinnerAdapter;
    private SimpleSpinnerAdapter citySpinnerAdapter;
    private SimpleSpinnerAdapter entityAdapter;

    private ApiInterface apiInterface;
    private Gson gson;

    private ProgressDialog progress;

    private LinearLayout grpBack;

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

        grpBack = v.findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null)
                    getActivity().finish();
            }
        });

        cityCodeMap = new HashMap<>();
        citiesMap = new HashMap<>();

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progress = new ProgressDialog(getContext());

        gson = new Gson();

        initializeUi(v);

        setOnClickListeners();

        getStateSpinnerData();

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

        stateSpinner = v.findViewById(R.id.spinner_state);
        citySpinner = v.findViewById(R.id.spinner_city);
        spinnerBusinessEntityType = v.findViewById(R.id.spinner_entity_type);

        btnCreateAccount = v.findViewById(R.id.grp_create_account);
        cbTc = v.findViewById(R.id.cb_tc);

        /*statesSpinnerAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, stateArr);
        citySpinnerAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, cities);*/
        entityAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, entities);

        /*spinnerState.setAdapter(statesSpinnerAdapter);
        spinnerCity.setAdapter(citySpinnerAdapter);*/
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
            Utility.showAlert(getContext(), "Info", "All fields are mandatory. Please make sure you've filled them all.");
        }
        else if(!ValidationUtils.isValidEmail(email)){
            Utility.showAlert(getContext(), "Info", "Please enter a valid email address.");
            etEmail.setError(AppConstants.VALIDATION_ERROR.EMAIL);
        }
        else if(phone.length() < 10){
            Utility.showAlert(getContext(), "Info", "Please enter an valid phone number.");
            etPhone.setError(AppConstants.VALIDATION_ERROR.PHONE);
        }
        else if(password.length() < 6) {
            Utility.showAlert(getContext(), "Info", "Password must be at least 6 characters long.");
            etPassword.setError(AppConstants.VALIDATION_ERROR.PASSWORD);
        }
        else if(pinCode.length() != 6) {
            Utility.showAlert(getContext(), "Info", "Please enter a valid 6-digit pincode.");
            etPinCode.setError(AppConstants.VALIDATION_ERROR.PIN);
        }
        else if(!cbTc.isChecked()){
            Utility.showAlert(getContext(), "Info", "Please agree to DMAC T&C to continue.");
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

        String stateCode = stateCodeMap.get(stateSpinner.getSelectedItem());
        String cityCode = cityCodeMap.get(citySpinner.getSelectedItem());

        String entityType = (String) spinnerBusinessEntityType.getSelectedItem();

        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("FullName", fullName);
        fieldMap.put("Email", email);
        fieldMap.put("Phone", phone);
        fieldMap.put("Password", password);
        fieldMap.put("BusinessLegalName", businessLegalName);
        fieldMap.put("BusinessShortName", businessShortName);
        fieldMap.put("BusinessAddress", businessAddress);
        fieldMap.put("CityCode", cityCode);
        fieldMap.put("StateCode", stateCode);
        fieldMap.put("PinCode", pinCode);
        fieldMap.put("TypeOfEntity", entityType);

        Call<QuickRegisterResponse> quickRegisterResponseCall = apiInterface.doQuickRegistration(fieldMap);
        quickRegisterResponseCall.enqueue(new Callback<QuickRegisterResponse>() {
            @Override
            public void onResponse(Call<QuickRegisterResponse> call, Response<QuickRegisterResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

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


    // Get data for state spinner:
    private void getStateSpinnerData() {

        progress.setMessage("Please wait...");
        progress.show();

        Call<StateResponse> stateResponseCall = apiInterface.doGetStates();
        stateResponseCall.enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                //progress.dismiss();

                if(response.body() == null)
                    return;

                Log.d(AppConstants.LOG_TAG, "stateResponseCall: "+ gson.toJson(response.body()));
                StateResponse stateResponse = response.body();
                stateList = stateResponse.getStateList();
                stateArr = new String[stateList.size()];
                stateCodeMap = new HashMap<>();
                for (int i = 0; i < stateList.size(); i++){
                    stateArr[i] = stateList.get(i).getStateName();
                    stateCodeMap.put(stateList.get(i).getStateName(), stateList.get(i).getStateCode());
                }

                getCitySpinnerData();
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getContext(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }


    // Get data for city spinner:
    private void getCitySpinnerData() {

        Call<CityResponse> cityResponseCall = apiInterface.doGetCities();
        cityResponseCall.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {

                if(response.body() == null)
                    return;

                Log.d(AppConstants.LOG_TAG, "stateResponseCall: "+ gson.toJson(response.body()));
                CityResponse cityResponse = response.body();
                cityList = cityResponse.getCityList();
                cityCodeMap = new HashMap<>();
                citiesMap = new HashMap<>();
                for(City city: cityList){
                    cityCodeMap.put(city.getCityName(), city.getCityCode());
                }

                for(State state: stateList) {
                    String stateCode = state.getStateCode();
                    List<City> cities = new ArrayList<>();
                    for (City city : cityList) {
                        if(city.getStateCode().equals(stateCode)){
                            cities.add(city);
                        }
                    }
                    String[] citiesArr = new String[cities.size()];

                    for (int i = 0; i < cities.size(); i++){
                        citiesArr[i] = cities.get(i).getCityName();
                    }

                    citiesMap.put(state.getStateName(), citiesArr);
                }
                progress.dismiss();
                setUpSpinners();

            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getContext(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

    // Set up spinners with downloaded city and state data:
    private void setUpSpinners() {

        SimpleSpinnerAdapter stateAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, stateArr);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateArr[position];
                SimpleSpinnerAdapter cityAdapter = new SimpleSpinnerAdapter(getContext(),
                        R.layout.spinner_layout, citiesMap.get(selectedState));
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(stateArr.length != 0) {
                    selectedState = stateArr[0];
                    SimpleSpinnerAdapter cityAdapter = new SimpleSpinnerAdapter(getContext(),
                            R.layout.spinner_layout, citiesMap.get(selectedState));
                    citySpinner.setAdapter(cityAdapter);
                }
            }
        });


    }
}
