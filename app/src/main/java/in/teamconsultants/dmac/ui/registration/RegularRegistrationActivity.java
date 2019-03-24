package in.teamconsultants.dmac.ui.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scanlibrary.ScanConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.City;
import in.teamconsultants.dmac.network.dto.CityResponse;
import in.teamconsultants.dmac.network.dto.CreateAccountResponse;
import in.teamconsultants.dmac.model.DirectorDetails;
import in.teamconsultants.dmac.model.DirectorDocuments;
import in.teamconsultants.dmac.model.State;
import in.teamconsultants.dmac.network.dto.StateResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import in.teamconsultants.dmac.utils.ValidationUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegularRegistrationActivity extends AppCompatActivity {

    // User Information:
    private EditText etFullName, etEmail, etPhone, etPassword;

    // Business Details:
    private EditText etBusinessLegalName, etBusinessShortName, etBusinessAddress, etPinCode;
    private EditText etGstrNum, etGstrUserId, etGstrPassword, etPAN,etTAN, etItrPassword;
    private Spinner stateSpinner, citySpinner, entitySpinner;
    private ImageView imgOthers, imgPartDeeds, imgRegCert, imgMOA, imgAOA, imgIncorpCert, imgGstReg, imgPan;
    private RadioGroup rgGstServices, rgITServices;
    private LinearLayout grpGstServices, grpITServices;
    private LinearLayout grpOthersCopy, grpPartnershipUploads, grpCompanyUploads;

    // CA/ITP Details:
    private EditText etCAName, etCAEmail, etCAPhone, etCAAddress;

    // Director Details:
    private LinearLayout grpDirectorDetails;
    private LinearLayout btnAddDirector;

    // Other Details:
    private CheckBox cbNewCompany;
    private CheckBox cbTC;
    private ImageView imgAuditedSheet;

    // Create Account:
    private LinearLayout btnCreateAccount;


    private SimpleSpinnerAdapter entityAdapter;
    private String[] entityTypeArr = {"Proprietary", "Partnership", "LLP", "Company", "Society", "Trust"};
    private SimpleSpinnerAdapter cityAdapter;
    private SimpleSpinnerAdapter stateAdapter;
    private List<State> stateList;
    private List<City> cityList;
    private String[] cityArr;
    private String[] stateArr;
    private Map<String, String[]> citiesMap;
    private Map<String, String> stateCodeMap;
    private Map<String, String> cityCodeMap;
    private String selectedState = null;

    private ApiInterface apiInterface;
    private ProgressDialog progress;
    private Gson gson;
    private SharedPreferences spUser;
    private String token;
    private Map<String, String> headerMap;

    private List<LinearLayout> directorsViewList;
    private int adminDirectorPos = -1;
    private LayoutInflater inflater;

    // Image file paths:
    private String imgOthersFilepath = "";
    private String imgMOAFilePath = "";
    private String imgAOAFilePath = "";
    private String imgPartDeedFilePath = "";
    private String imgRegCertFilePath = "";
    private String imgIncorpCertFilePath = "";
    private String imgGSTRegFilePath = "";
    private String imgPANFilePath = "";
    private String imgAuditBalSheetFilepath = "";
    private Map<Integer, DirectorDocuments> directorDocumentsMap;

    private LinearLayout grpBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_registration);

        grpBack = findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        gson = new Gson();

        directorsViewList = new ArrayList<>();
        inflater = getLayoutInflater();
        directorDocumentsMap = new HashMap<>();

        spUser = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = spUser.getString(AppConstants.SP.TAG_TOKEN, "");
        headerMap = new HashMap<>();
        headerMap.put("TOKEN", token);

        initializeUi();

        getStateSpinnerData();

    }


    // Initialize UI views:
    private void initializeUi() {

        // User information:
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);

        // Business details:
        etBusinessLegalName = findViewById(R.id.et_business_legal_name);
        etBusinessShortName = findViewById(R.id.et_business_short_name);
        etBusinessAddress = findViewById(R.id.et_business_address);
        etPinCode = findViewById(R.id.et_pin_code);
        etGstrNum = findViewById(R.id.et_gstr_number);
        etGstrUserId = findViewById(R.id.et_gstr_user_id);
        etGstrPassword = findViewById(R.id.et_gstr_password);
        etPAN = findViewById(R.id.et_pan_number);
        etTAN = findViewById(R.id.et_tan_num);
        etItrPassword = findViewById(R.id.et_itr_password);
        stateSpinner = findViewById(R.id.spinner_state);
        citySpinner = findViewById(R.id.spinner_city);
        entitySpinner = findViewById(R.id.spinner_entity_type);
        imgOthers = findViewById(R.id.img_others_copy);
        imgAOA = findViewById(R.id.img_aoa_copy);
        imgGstReg = findViewById(R.id.img_gst_copy);
        imgIncorpCert = findViewById(R.id.img_incorp_cert);
        imgMOA = findViewById(R.id.img_moa_copy);
        imgPan = findViewById(R.id.img_pan_copy);
        imgPartDeeds = findViewById(R.id.img_part_deed_copy);
        imgRegCert = findViewById(R.id.img_reg_cert);
        rgGstServices = findViewById(R.id.rg_gst);
        rgITServices = findViewById(R.id.rg_it);
        grpOthersCopy = findViewById(R.id.grp_others_copy);
        grpCompanyUploads = findViewById(R.id.grp_company_uploads);
        grpPartnershipUploads = findViewById(R.id.grp_partnership_uploads);
        grpGstServices = findViewById(R.id.card_gst_info);
        grpITServices = findViewById(R.id.card_it_info);

        // CA/ITP Details:
        etCAName = findViewById(R.id.et_ca_name);
        etCAEmail = findViewById(R.id.et_ca_email);
        etCAPhone = findViewById(R.id.et_ca_phone);
        etCAAddress = findViewById(R.id.et_ca_address);

        // Director Details:
        grpDirectorDetails = findViewById(R.id.grp_director_info);
        btnAddDirector = findViewById(R.id.grp_add_director);

        // Other Details:
        cbNewCompany = findViewById(R.id.cb_new_company);
        cbTC = findViewById(R.id.cb_tc);
        imgAuditedSheet = findViewById(R.id.img_balance_sheet);

        // Create Account:
        btnCreateAccount = findViewById(R.id.grp_create_account);

        addNewDirector();
        //adminDirectorPos = 0;

        setOnClickListeners();

    }


    // Set on-click listeners for the UI views:
    private void setOnClickListeners() {

        rgITServices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_it_yes:
                        grpITServices.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_it_no:
                        grpITServices.setVisibility(View.GONE);
                        break;
                    default:
                        grpITServices.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgGstServices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_gst_yes:
                        grpGstServices.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_gst_no:
                        grpGstServices.setVisibility(View.GONE);
                        break;
                    default:
                        grpGstServices.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnAddDirector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDirector();
                if(directorsViewList.size() == AppConstants.MAX_ALLOWED_DIRECTORS){
                    btnAddDirector.setVisibility(View.GONE);
                }
                else {
                    btnAddDirector.setVisibility(View.VISIBLE);
                }
            }
        });

        imgOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.OTHERS);
            }
        });

        imgRegCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.REG_CERT);
            }
        });

        imgIncorpCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.INCORP_CERT);
            }
        });

        imgMOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.MOA);
            }
        });

        imgAOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.AOA);
            }
        });

        imgPartDeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.PARTNERSHIP_DEED);
            }
        });

        imgPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.PAN);
            }
        });

        imgGstReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.GST_REG);
            }
        });

        imgAuditedSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.AUDIT_BAL_SHEET);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserInputs();
            }
        });

    }

    //  Add new director layout:
    private void addNewDirector() {
        LinearLayout newDirectorLayout = (LinearLayout) inflater.inflate(R.layout.card_director_info, null);
        setUpNewDirectorLayout(newDirectorLayout);
        grpDirectorDetails.addView(newDirectorLayout);
        directorsViewList.add(newDirectorLayout);
    }


    // Set up views inside the given directorLayout:
    private void setUpNewDirectorLayout(final LinearLayout directorLayout) {
        LinearLayout btnRemoveDirector = directorLayout.findViewById(R.id.grp_remove_director);
        if(directorsViewList.size() == 0){
            btnRemoveDirector.setVisibility(View.GONE);
        }
        else {
            btnRemoveDirector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grpDirectorDetails.removeView(directorLayout);
                    directorsViewList.remove(directorLayout);
                    if(btnAddDirector.getVisibility() == View.GONE){
                        btnAddDirector.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        CheckBox cbMakeAdmin = directorLayout.findViewById(R.id.cb_make_director_admin);
        cbMakeAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(adminDirectorPos != -1) {
                        LinearLayout oldCheckedLayout = directorsViewList.get(adminDirectorPos);
                        CheckBox cbOldAdmin = oldCheckedLayout.findViewById(R.id.cb_make_director_admin);
                        cbOldAdmin.setChecked(false);
                    }
                    adminDirectorPos = directorsViewList.indexOf(directorLayout);
                }
                else {
                    adminDirectorPos = -1;
                }
            }
        });

        ImageView imgDirectorAadhar = directorLayout.findViewById(R.id.img_director_aadhar_copy);
        ImageView imgDirectorPAN = directorLayout.findViewById(R.id.img_director_pan_copy);

        imgDirectorAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagePos = ((directorsViewList.indexOf(directorLayout) + 1)* 10) + 1;
                selectImage(imagePos);
            }
        });

        imgDirectorPAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imagePos = ((directorsViewList.indexOf(directorLayout)+ 1) * 10) + 2;
                selectImage(imagePos);
            }
        });


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
                Utility.showAlert(RegularRegistrationActivity.this, "Error", "An unknown error occurred, please try again");
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
                Utility.showAlert(RegularRegistrationActivity.this, "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }


    // Set up spinners with downloaded city and state data:
    private void setUpSpinners() {

        entityAdapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout, entityTypeArr);
        entitySpinner.setAdapter(entityAdapter);
        entitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedEntity = entityTypeArr[position];
                if(selectedEntity.equals(AppConstants.ENTITY_TYPE.COMPANY)){
                    grpOthersCopy.setVisibility(View.GONE);
                    grpCompanyUploads.setVisibility(View.VISIBLE);
                    grpPartnershipUploads.setVisibility(View.GONE);
                }
                else if(selectedEntity.equals(AppConstants.ENTITY_TYPE.PARTNERSHIP)){
                    grpOthersCopy.setVisibility(View.GONE);
                    grpCompanyUploads.setVisibility(View.GONE);
                    grpPartnershipUploads.setVisibility(View.VISIBLE);
                }
                else {
                    grpOthersCopy.setVisibility(View.VISIBLE);
                    grpCompanyUploads.setVisibility(View.GONE);
                    grpPartnershipUploads.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        SimpleSpinnerAdapter stateAdapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout, stateArr);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateArr[position];
                SimpleSpinnerAdapter cityAdapter = new SimpleSpinnerAdapter(RegularRegistrationActivity.this,
                        R.layout.spinner_layout, citiesMap.get(selectedState));
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(stateArr.length != 0) {
                    selectedState = stateArr[0];
                    SimpleSpinnerAdapter cityAdapter = new SimpleSpinnerAdapter(RegularRegistrationActivity.this,
                            R.layout.spinner_layout, citiesMap.get(selectedState));
                    citySpinner.setAdapter(cityAdapter);
                }
            }
        });


    }


    // Select an image from gallery for the given image position:
    private void selectImage(int imagePosition) {
        if(Utility.isPermissionsGranted(RegularRegistrationActivity.this)) {
           /* Intent intent = new Intent();
            intent.putExtra("position", imagePosition);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, imagePosition);*/
            Utility.showImageSelectionDialog(this, imagePosition);
        }
        else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Please grant the requested permissions when prompted to continue");
            alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utility.askForPermissions(RegularRegistrationActivity.this);
                }
            });
            alertBuilder.setCancelable(false);
            alertBuilder.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){

            Uri path = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);

            String filePath = "";

            filePath = Utility.getRealPathFromUri(this, path);


            Log.d(AppConstants.LOG_TAG, "Media Path Received: "+filePath);


            try {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                setBitmapToImgView(requestCode, path, filePath);

            }
            catch (Exception e){
                Toast.makeText(RegularRegistrationActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }


    // Set the bitmap to the respective ImageViews and save filepath:
    private void setBitmapToImgView(int requestCode, Uri path, String filePath) {

        ImageView reqImageView = null;

        if(requestCode < 10){


            switch (requestCode){
                case AppConstants.BUSINESS_DOC_TYPE.OTHERS:
                    reqImageView = imgOthers;
                    imgOthersFilepath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.PARTNERSHIP_DEED:
                    reqImageView = imgPartDeeds;
                    imgPartDeedFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.AOA:
                    reqImageView = imgAOA;
                    imgAOAFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.MOA:
                    reqImageView = imgMOA;
                    imgMOAFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.INCORP_CERT:
                    reqImageView = imgIncorpCert;
                    imgIncorpCertFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.REG_CERT:
                    reqImageView = imgRegCert;
                    imgRegCertFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.PAN:
                    reqImageView = imgPan;
                    imgPANFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.GST_REG:
                    reqImageView = imgGstReg;
                    imgGSTRegFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.AUDIT_BAL_SHEET:
                    reqImageView = imgAuditedSheet;
                    imgAuditBalSheetFilepath = filePath;
                    break;
            }

        }
        else {
            int index = (requestCode / 10) - 1;
            int type = requestCode % 10;

            LinearLayout directorLayout = directorsViewList.get(index);


            DirectorDocuments directorDocuments;
            if(directorDocumentsMap.containsKey(index)){
                directorDocuments = directorDocumentsMap.get(index);
            }
            else {
                directorDocuments = new DirectorDocuments();
            }

            if(type == AppConstants.DIRECTOR_DOC_TYPE.AADHAR){
                ImageView imgDirectorAadhar = directorLayout.findViewById(R.id.img_director_aadhar_copy);
                reqImageView = imgDirectorAadhar;
                directorDocuments.setImgAadharFilePath(filePath);
            }
            else if(type == AppConstants.DIRECTOR_DOC_TYPE.PAN){
                ImageView imgDirectorPAN = directorLayout.findViewById(R.id.img_director_pan_copy);
                reqImageView = imgDirectorPAN;
                directorDocuments.setImgPANFilePath(filePath);
            }

            directorDocumentsMap.put(index, directorDocuments);

        }


        if(null != reqImageView){
            Picasso.get().load(path).resize(0, 500).into(reqImageView);
        }


    }

    // User Info:
    private String fullName;
    private String email;
    private String phoneNum;
    private String password;

    // Business Details:
    private String businessLegalName;
    private String businessShortName;
    private String businessAddress;
    private String pinCode;
    private String stateCode;
    private String cityCode;
    private String typeOfEntity;
    private String gstrNum;
    private String gstrUserId;
    private String gstrPassword;
    private String panNumber;
    private String tanNumber;
    private String itrPassword;

    // CA/ITP Details:
    private String caName;
    private String caEmail;
    private String caPhone;
    private String caAddress;

    // Director Details:
    private List<DirectorDetails> directorDetailsList;

    // Other Details:
    private boolean isNewCompany;
    private boolean isAgreeTC;


    private void validateUserInputs() {
        // User Info:
        fullName = etFullName.getText().toString();
        email = etEmail.getText().toString();
        phoneNum = etPhone.getText().toString();
        password = etPassword.getText().toString();

        // Validate User Info:
        if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password)){
            Utility.showAlert(this, "Info", "Please enter valid user information");
            return;
        }
        else if(!Utility.isValidEmail(email)){
            Utility.showAlert(this, "Info", "Please enter valid user email");
            return;
        }
        else if(phoneNum.length() < 10){
            Utility.showAlert(this, "Info", "Please enter a valid user phone number");
            return;
        }
        else if(password.length() < 6){
            Utility.showAlert(this, "Info", "Password length must be more than or equal to 6 characters");
            return;
        }

        // Business Details
        businessLegalName = etBusinessLegalName.getText().toString();
        businessShortName = etBusinessShortName.getText().toString();
        businessAddress = etBusinessAddress.getText().toString();
        pinCode = etPinCode.getText().toString();

        if(TextUtils.isEmpty(businessLegalName) || TextUtils.isEmpty(businessShortName)
                || TextUtils.isEmpty(businessAddress) || TextUtils.isEmpty(pinCode)){
            Utility.showAlert(this, "Info", "Business details can't be left empty");
            return;
        }

        stateCode = stateCodeMap.get(stateSpinner.getSelectedItem());
        cityCode = cityCodeMap.get(citySpinner.getSelectedItem());
        typeOfEntity = (String) entitySpinner.getSelectedItem();

        if(typeOfEntity.equals(AppConstants.ENTITY_TYPE.PARTNERSHIP)){
            if(null == imgPartDeedFilePath || null == imgRegCertFilePath ||  TextUtils.isEmpty(imgPartDeedFilePath)
                    || TextUtils.isEmpty(imgRegCertFilePath)){
                Utility.showAlert(this, "Info", "Please choose valid business documents");
                return;
            }
        }
        else if(typeOfEntity.equals(AppConstants.ENTITY_TYPE.COMPANY)){
            if(null == imgMOAFilePath || null == imgAOAFilePath || null == imgIncorpCertFilePath || TextUtils.isEmpty(imgMOAFilePath)
                    ||  TextUtils.isEmpty(imgAOAFilePath) ||  TextUtils.isEmpty(imgIncorpCertFilePath)){
                Utility.showAlert(this, "Info", "Please choose valid business documents");
                return;
            }
        }
        else {
            if(null == imgOthersFilepath || TextUtils.isEmpty(imgOthersFilepath)){
                Utility.showAlert(this, "Info", "Please choose valid business documents");
                return;
            }
        }

        gstrNum = etGstrNum.getText().toString();
        gstrUserId = etGstrUserId.getText().toString();
        gstrPassword = etGstrPassword.getText().toString();

        if(!ValidationUtils.isValidGSTIN(gstrNum)){
            Utility.showAlert(this, "Info", "Please enter a valid GSTIN Number");
            return;
        }

        if(rgGstServices.getCheckedRadioButtonId() == R.id.rb_gst_yes){
            if(TextUtils.isEmpty(gstrNum) || TextUtils.isEmpty(gstrUserId) || TextUtils.isEmpty(gstrPassword)){
                Utility.showAlert(this, "Info", "Please fill in all the GST details");
                return;
            }

            if(null == imgGSTRegFilePath || TextUtils.isEmpty(imgGSTRegFilePath)){
                Utility.showAlert(this, "Info", "Please choose a valid GST Registration Copy");
                return;
            }
        }

        panNumber = etPAN.getText().toString();
        tanNumber = etTAN.getText().toString();
        itrPassword = etItrPassword.getText().toString();


        if(!ValidationUtils.isValidPAN(panNumber)){
            Utility.showAlert(this, "Info", "Please enter a valid PAN Number");
            return;
        }

        if(rgITServices.getCheckedRadioButtonId() == R.id.rb_it_yes){
            if(TextUtils.isEmpty(panNumber) || TextUtils.isEmpty(tanNumber) || TextUtils.isEmpty(itrPassword)){
                Utility.showAlert(this, "Info", "Please fill in all the Income Tax details");
                return;
            }

            if(null == imgPANFilePath || TextUtils.isEmpty(imgPANFilePath)){
                Utility.showAlert(this, "Info", "Please choose a valid PAN Card Copy");
                return;
            }

        }

        // CA/ITP Details:
        caName = etCAName.getText().toString();
        caEmail = etCAEmail.getText().toString();
        caPhone = etCAPhone.getText().toString();
        caAddress = etCAAddress.getText().toString();

        if(TextUtils.isEmpty(caName) || TextUtils.isEmpty(caEmail) || TextUtils.isEmpty(caPhone)
                || TextUtils.isEmpty(caAddress)){
            Utility.showAlert(this, "Info", "Please fill all the details of CA");
            return;
        }
        else if(!Utility.isValidEmail(caEmail)) {
            Utility.showAlert(this, "Info","Please enter a valid CA Email Address");
        }


        // Director Details:
        boolean isValidDirectorData = false;
        directorDetailsList = new ArrayList<>();
        for(int i = 0; i < directorsViewList.size(); i++){
            LinearLayout directorLayout = directorsViewList.get(i);
            EditText etDirectorFullName = directorLayout.findViewById(R.id.et_director_full_name);
            EditText etDirectorEmail = directorLayout.findViewById(R.id.et_director_email);
            EditText etDirectorPhone = directorLayout.findViewById(R.id.et_director_phone);
            EditText etDirectorAadhar = directorLayout.findViewById(R.id.et_director_aadhar);
            EditText etDirectorPAN = directorLayout.findViewById(R.id.et_director_pan);

            CheckBox cbIsAdmin = directorLayout.findViewById(R.id.cb_make_director_admin);

            String directorFullName = etDirectorFullName.getText().toString();
            String directorEmail = etDirectorEmail.getText().toString();
            String directorPhone = etDirectorPhone.getText().toString();
            String directorAadhar = etDirectorAadhar.getText().toString();
            String directorPAN = etDirectorPAN.getText().toString();

            if(TextUtils.isEmpty(directorFullName) || TextUtils.isEmpty(directorEmail) || TextUtils.isEmpty(directorPhone)
                    || TextUtils.isEmpty(directorAadhar) || TextUtils.isEmpty(directorPAN)){
                Utility.showAlert(this, "Info", "Please fill in all the details");
                isValidDirectorData = false;
                break;
            }

            DirectorDocuments directorDocuments = directorDocumentsMap.get(i);
            if(null == directorDocuments.getImgPANFilePath() || null == directorDocuments.getImgPANFilePath()
                    || TextUtils.isEmpty(directorDocuments.getImgAadharFilePath()) || TextUtils.isEmpty(directorDocuments.getImgPANFilePath())){
                Utility.showAlert(this, "Info", "Please choose all the required documents");
                isValidDirectorData = false;
                break;
            }

            DirectorDetails directorDetails = new DirectorDetails();
            directorDetails.setFullName(directorFullName);
            directorDetails.setAadhar(directorAadhar);
            directorDetails.setEmail(directorEmail);
            directorDetails.setPan(directorPAN);
            directorDetails.setPhone(directorPhone);
            directorDetails.setAdmin(cbIsAdmin.isChecked());
            directorDetails.setDirectorDocuments(directorDocuments);

            directorDetailsList.add(directorDetails);

            isValidDirectorData = true;
        }

        if(!isValidDirectorData){
            return;
        }


        // Other details:
        isNewCompany = cbNewCompany.isChecked();

        if(null == imgAuditBalSheetFilepath || TextUtils.isEmpty(imgAuditBalSheetFilepath)){
            Utility.showAlert(this, "Info", "Please choose previous year audited balance sheet");
            return;
        }

        isAgreeTC = cbTC.isChecked();

        if(!isAgreeTC){
            Utility.showAlert(this, "Info", "Please agree to DMAC Terms & Conditions to continue");
            return;
        }

        progress.setMessage("Creating account...");
        progress.show();
        startDataUpload();

    }

    private void startDataUpload() {

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        List<MultipartBody.Part> multiPartList = new ArrayList<>();

        // User Details:
        requestBodyMap.put("FullName", Utility.getTextRequestBody(fullName));
        requestBodyMap.put("Email", Utility.getTextRequestBody(email));
        requestBodyMap.put("Phone", Utility.getTextRequestBody(phoneNum));
        requestBodyMap.put("Password", Utility.getTextRequestBody(password));

        // CA/ITP Details:
        requestBodyMap.put("NameOfCAITP", Utility.getTextRequestBody(caName));
        requestBodyMap.put("EmailOfCAITP", Utility.getTextRequestBody(caEmail));
        requestBodyMap.put("PhoneOfCAITP", Utility.getTextRequestBody(caPhone));
        requestBodyMap.put("AddressOfCAITP", Utility.getTextRequestBody(caAddress));

        // Business Details:
        requestBodyMap.put("BusinessLegalName", Utility.getTextRequestBody(businessLegalName));
        requestBodyMap.put("BusinessShortName", Utility.getTextRequestBody(businessShortName));
        requestBodyMap.put("BusinessAddress", Utility.getTextRequestBody(businessAddress));
        requestBodyMap.put("PinCode", Utility.getTextRequestBody(pinCode));
        requestBodyMap.put("StateCode", Utility.getTextRequestBody(stateCode));
        requestBodyMap.put("CityCode", Utility.getTextRequestBody(cityCode));
        requestBodyMap.put("TypeOfEntity", Utility.getTextRequestBody(typeOfEntity));

        // GST Services:
        String gstServices = (rgGstServices.getCheckedRadioButtonId() == R.id.rb_gst_yes) ? "1" : "0";
        requestBodyMap.put("GSTService", Utility.getTextRequestBody(gstServices));
        requestBodyMap.put("GSTRNo", Utility.getTextRequestBody(gstrNum));
        requestBodyMap.put("GSTPortalUserID", Utility.getTextRequestBody(gstrUserId));
        requestBodyMap.put("GSTPortalPassword", Utility.getTextRequestBody(gstrPassword));

        // IT Services:
        String itServices = (rgITServices.getCheckedRadioButtonId() == R.id.rb_it_yes) ? "1" : "0";
        requestBodyMap.put("IncomeTaxService", Utility.getTextRequestBody(itServices));
        requestBodyMap.put("PANNo", Utility.getTextRequestBody(panNumber));
        requestBodyMap.put("TANNo", Utility.getTextRequestBody(tanNumber));
        requestBodyMap.put("ITRPassword", Utility.getTextRequestBody(itrPassword));

        if(!TextUtils.isEmpty(imgOthersFilepath))
            multiPartList.add(Utility.getMultipartImage("OthersDocFile", imgOthersFilepath));

        if(!TextUtils.isEmpty(imgMOAFilePath))
            multiPartList.add(Utility.getMultipartImage("MOAFile", imgMOAFilePath));

        if(!TextUtils.isEmpty(imgAOAFilePath))
            multiPartList.add(Utility.getMultipartImage("AOAFile", imgAOAFilePath));

        if(!TextUtils.isEmpty(imgPartDeedFilePath))
            multiPartList.add(Utility.getMultipartImage("PartnershipDeedFile", imgPartDeedFilePath));

        if(!TextUtils.isEmpty(imgRegCertFilePath))
            multiPartList.add(Utility.getMultipartImage("RegistrationCertificateFile", imgRegCertFilePath));

        if(!TextUtils.isEmpty(imgIncorpCertFilePath))
            multiPartList.add(Utility.getMultipartImage("IncorporationCertificateFile", imgIncorpCertFilePath));

        if(!TextUtils.isEmpty(imgGSTRegFilePath))
            multiPartList.add(Utility.getMultipartImage("GSTRegistrationFile", imgGSTRegFilePath));

        if(!TextUtils.isEmpty(imgPANFilePath))
            multiPartList.add(Utility.getMultipartImage("PANCardFile", imgPANFilePath));

        if(!TextUtils.isEmpty(imgAuditBalSheetFilepath))
            multiPartList.add(Utility.getMultipartImage("PreviousYearAuditSheetFile", imgAuditBalSheetFilepath));

        // Director Details:
        for(int i = 0; i < directorDetailsList.size(); i++){

            DirectorDetails directorDetails = directorDetailsList.get(i);
            requestBodyMap.put("DirectorFullName["+i+"]", Utility.getTextRequestBody(directorDetails.getFullName()));
            requestBodyMap.put("DirectorEmail["+i+"]", Utility.getTextRequestBody(directorDetails.getEmail()));
            requestBodyMap.put("DirectorPhone["+i+"]", Utility.getTextRequestBody(directorDetails.getPhone()));
            requestBodyMap.put("DirectorPAN["+i+"]", Utility.getTextRequestBody(directorDetails.getPan()));
            requestBodyMap.put("DirectorAadhar["+i+"]", Utility.getTextRequestBody(directorDetails.getAadhar()));

            if(!TextUtils.isEmpty(directorDetails.getDirectorDocuments().getImgAadharFilePath()))
                multiPartList.add(Utility.getMultipartImage("DirectorAdharFile["+i+"]",
                        directorDetails.getDirectorDocuments().getImgAadharFilePath()));

            if(!TextUtils.isEmpty(directorDetails.getDirectorDocuments().getImgPANFilePath()))
                multiPartList.add(Utility.getMultipartImage("DirectorPANFile["+i+"]",
                        directorDetails.getDirectorDocuments().getImgPANFilePath()));

        }


        Call<CreateAccountResponse> createAccountResponseCall = apiInterface.doCreateAccount(headerMap,
                requestBodyMap, multiPartList);

        createAccountResponseCall.enqueue(new Callback<CreateAccountResponse>() {
            @Override
            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

                //Log.d(AppConstants.LOG_TAG, "createAccountResponseCall:" + gson.toJson(response));
                CreateAccountResponse createAccountResponse = response.body();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegularRegistrationActivity.this);
                if(createAccountResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    alertBuilder.setTitle("Success");
                    alertBuilder.setCancelable(false);
                    alertBuilder.setMessage("Account created successfully");
                    alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertBuilder.show();
                }
                else {
                    if(createAccountResponse.getDetails().getEmail() != null){
                        Utility.showAlert(RegularRegistrationActivity.this, "Info", "Email is already registered, please try again");
                    }
                    else {
                        Utility.showAlert(RegularRegistrationActivity.this, "Error", "Something went wrong, please try again");
                    }
                }

            }

            @Override
            public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(RegularRegistrationActivity.this, "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

}