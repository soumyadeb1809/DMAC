package in.teamconsultants.dmac.ui.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.City;
import in.teamconsultants.dmac.model.CityResponse;
import in.teamconsultants.dmac.model.DirectorDocuments;
import in.teamconsultants.dmac.model.State;
import in.teamconsultants.dmac.model.StateResponse;
import in.teamconsultants.dmac.network.ApiClient;
import in.teamconsultants.dmac.network.ApiInterface;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
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

    private List<LinearLayout> directorsViewList;
    private int adminDirectorPos = -1;
    private LayoutInflater inflater;

    // Image file paths:
    private String imgOthersFilepath = null;
    private String imgMOAFilePath = null;
    private String imgAOAFilePath = null;
    private String imgPartDeedFilePath = null;
    private String imgRegCertFilePath = null;
    private String imgIncorpCertFilePath = null;
    private String imgPANFilePath = null;
    private String imgAuditBalSheetFilepath = null;
    private Map<Integer, DirectorDocuments> directorDocumentsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_registration);

        progress = new ProgressDialog(this);
        gson = new Gson();

        directorsViewList = new ArrayList<>();
        inflater = getLayoutInflater();
        directorDocumentsMap = new HashMap<>();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

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

        imgAuditedSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AppConstants.BUSINESS_DOC_TYPE.AUDIT_BAL_SHEET);
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
        Intent intent = new Intent();
        intent.putExtra("position", imagePosition);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, imagePosition);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){

            Uri path = data.getData();

            String wholeID = DocumentsContract.getDocumentId(path);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);

            String filePath = "";

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            Log.d(AppConstants.LOG_TAG, "Media Path Received: "+filePath);

            //jobFileUriMap.put(requestCode, filePath);

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                setBitmapToImgView(requestCode, bitmap, filePath);

            }
            catch (Exception e){
                Toast.makeText(RegularRegistrationActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }


    // Set the bitmap to the respective ImageViews and save filepath:
    private void setBitmapToImgView(int requestCode, Bitmap bitmap, String filePath) {

        if(requestCode < 10){

            switch (requestCode){
                case AppConstants.BUSINESS_DOC_TYPE.OTHERS:
                    imgOthers.setImageBitmap(bitmap);
                    imgOthersFilepath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.PARTNERSHIP_DEED:
                    imgPartDeeds.setImageBitmap(bitmap);
                    imgPartDeedFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.AOA:
                    imgAOA.setImageBitmap(bitmap);
                    imgAOAFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.MOA:
                    imgMOA.setImageBitmap(bitmap);
                    imgMOAFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.INCORP_CERT:
                    imgIncorpCert.setImageBitmap(bitmap);
                    imgIncorpCertFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.REG_CERT:
                    imgRegCert.setImageBitmap(bitmap);
                    imgRegCertFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.PAN:
                    imgPan.setImageBitmap(bitmap);
                    imgPANFilePath = filePath;
                    break;
                case AppConstants.BUSINESS_DOC_TYPE.AUDIT_BAL_SHEET:
                    imgAuditedSheet.setImageBitmap(bitmap);
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
                imgDirectorAadhar.setImageBitmap(bitmap);
                directorDocuments.setImgAadharFilePath(filePath);
            }
            else if(type == AppConstants.DIRECTOR_DOC_TYPE.PAN){
                ImageView imgDirectorPAN = directorLayout.findViewById(R.id.img_director_pan_copy);
                imgDirectorPAN.setImageBitmap(bitmap);
                directorDocuments.setImgPANFilePath(filePath);
            }

            directorDocumentsMap.put(index, directorDocuments);

        }

    }

}