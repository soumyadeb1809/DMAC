package in.teamconsultants.dmac.ui.home.accounts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.image.ImageDisplayActivity;
import in.teamconsultants.dmac.model.AccountDataObj;
import in.teamconsultants.dmac.network.dto.AccountDetailResponse;
import in.teamconsultants.dmac.model.City;
import in.teamconsultants.dmac.network.dto.CityResponse;
import in.teamconsultants.dmac.model.DirectorDataObj;
import in.teamconsultants.dmac.model.State;
import in.teamconsultants.dmac.network.dto.StateResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountDetailActivity extends AppCompatActivity {


    // User Information:
    private EditText etFullName, etEmail, etPhone, etPassword;

    // Business Details:
    private EditText etBusinessLegalName, etBusinessShortName, etBusinessAddress, etPinCode;
    private EditText etGstrNum, etGstrUserId, etGstrPassword, etPAN,etTAN, etItrPassword;
    private EditText etState, etCity, etEntity;
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
    private ImageView imgAuditedSheet;


    private Toolbar toolbar;


    private ApiInterface apiInterface;
    private Gson gson;
    private SharedPreferences spUser;
    private ProgressDialog progress;
    private String token;
    private Map<String, String> headerMap;
    private String accountId;
    private AccountDetailResponse accountDetails;

    private List<State> stateList;

    private Map<String, String> stateCodeMap;
    private Map<String, String> cityCodeMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
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

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        spUser = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = spUser.getString(AppConstants.SP.TAG_TOKEN, "");
        headerMap = new HashMap<>();
        headerMap.put("TOKEN", token);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        accountId = "81";
        
        loadData();
        
        initializeUi();

    }

    private void initializeUi() {


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
        etState = findViewById(R.id.et_state);
        etCity = findViewById(R.id.et_city);
        etEntity = findViewById(R.id.et_business_type);
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

        // Other Details:
        imgAuditedSheet = findViewById(R.id.img_balance_sheet);

    }

    private void loadData() {
        progress.setMessage("Loading account details...");
        progress.show();

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("AccountId", accountId);
        Call<AccountDetailResponse> accountDetailResponseCall = apiInterface.doGetAccountDetails(headerMap, queryMap);

        accountDetailResponseCall.enqueue(new Callback<AccountDetailResponse>() {
            @Override
            public void onResponse(Call<AccountDetailResponse> call, Response<AccountDetailResponse> response) {
                accountDetails = response.body();
                getCityData();
            }

            @Override
            public void onFailure(Call<AccountDetailResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                Utility.showAlert(AccountDetailActivity.this, "Error", "Something went wrong, please try again");
            }
        });

    }

    private void getStateData() {

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
                stateCodeMap = new HashMap<>();
                for (int i = 0; i < stateList.size(); i++){
                    stateCodeMap.put(stateList.get(i).getStateName(), stateList.get(i).getStateCode());
                }


            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(AccountDetailActivity.this, "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }


    // Get data for city spinner:
    private void getCityData() {

        Call<CityResponse> cityResponseCall = apiInterface.doGetCities();
        cityResponseCall.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {

                Log.d(AppConstants.LOG_TAG, "stateResponseCall: "+ gson.toJson(response.body()));
                CityResponse cityResponse = response.body();
                cityCodeMap = new HashMap<>();
                for(City city: cityResponse.getCityList()){
                    cityCodeMap.put(city.getCityName(), city.getCityCode());
                }

                populateUi();
                progress.dismiss();

            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(AccountDetailActivity.this, "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }

    private void populateUi() {

        // Business Details:
        AccountDataObj accountData = accountDetails.getAccountData();
        etBusinessLegalName.setText(accountData.getBusinessLegalName());
        etBusinessShortName.setText(accountData.getBusinessShortName());
        etBusinessAddress.setText(accountData.getBusinessAddress());
        if(null == cityCodeMap || null == cityCodeMap.get(accountData.getCityCode())){
            etCity.setText("NA");
        }
        else {
            etCity.setText( cityCodeMap.get(accountData.getCityCode()));
        }
        etPinCode.setText(accountData.getPinCode());

        String entityType = "Company";

        if(entityType.equals(AppConstants.ENTITY_TYPE.COMPANY)){
            grpOthersCopy.setVisibility(View.GONE);
            grpCompanyUploads.setVisibility(View.VISIBLE);
            grpPartnershipUploads.setVisibility(View.GONE);

            String moaFilepath = accountData.getMOAFilePath();
            Log.d(AppConstants.LOG_TAG, "MOA Filepath: " + AppConstants.FILE_BASE_URL+moaFilepath);
            if(!TextUtils.isEmpty(moaFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+moaFilepath)
                        .into(imgMOA).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgMOA, AppConstants.FILE_BASE_URL+moaFilepath);
            }
            else {
                imgMOA.setImageResource(R.drawable.img_placeholder);
            }

            String aoaFilepath = accountData.getAOAFilePath();
            Log.d(AppConstants.LOG_TAG, "AOA Filepath: " + AppConstants.FILE_BASE_URL+aoaFilepath);
            if(!TextUtils.isEmpty(aoaFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+aoaFilepath)
                        .into(imgAOA).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgAOA, AppConstants.FILE_BASE_URL+aoaFilepath);
            }
            else {
                imgAOA.setImageResource(R.drawable.img_placeholder);
            }

            String incorpCertFilepath = accountData.getIncorporationCertificateFilePath();
            Log.d(AppConstants.LOG_TAG, "IncoprCert Filepath: " + AppConstants.FILE_BASE_URL+incorpCertFilepath);
            if(!TextUtils.isEmpty(incorpCertFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+incorpCertFilepath)
                        .into(imgIncorpCert).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgIncorpCert, AppConstants.FILE_BASE_URL+incorpCertFilepath);
            }
            else {
                imgIncorpCert.setImageResource(R.drawable.img_placeholder);
            }


        }
        else if(entityType.equals(AppConstants.ENTITY_TYPE.PARTNERSHIP)){
            grpOthersCopy.setVisibility(View.GONE);
            grpCompanyUploads.setVisibility(View.GONE);
            grpPartnershipUploads.setVisibility(View.VISIBLE);

            String partDeedFilepath = accountData.getPartnershipDeedFilePath();
            Log.d(AppConstants.LOG_TAG, "PartDeed Filepath: " + AppConstants.FILE_BASE_URL+partDeedFilepath);
            if(!TextUtils.isEmpty(accountData.getPartnershipDeedFilePath())){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+partDeedFilepath)
                        .into(imgPartDeeds).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgPartDeeds, AppConstants.FILE_BASE_URL+partDeedFilepath);
            }
            else {
                imgPartDeeds.setImageResource(R.drawable.img_placeholder);
            }

            String regCertFilepath = accountData.getRegistrationCertificateFilePath();
            Log.d(AppConstants.LOG_TAG, "RegCert Filepath: " + AppConstants.FILE_BASE_URL+regCertFilepath);
            if(!TextUtils.isEmpty(regCertFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+regCertFilepath)
                        .into(imgRegCert).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgRegCert, AppConstants.FILE_BASE_URL+regCertFilepath);
            }
            else {
                imgRegCert.setImageResource(R.drawable.img_placeholder);
            }
        }
        else {
            grpOthersCopy.setVisibility(View.VISIBLE);
            grpCompanyUploads.setVisibility(View.GONE);
            grpPartnershipUploads.setVisibility(View.GONE);

            String othersFilepath = accountData.getOthersDocFilePath();
            Log.d(AppConstants.LOG_TAG, "Others Filepath: " + AppConstants.FILE_BASE_URL+othersFilepath);
            if(!TextUtils.isEmpty(othersFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+othersFilepath)
                        .into(imgOthers).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgOthers, AppConstants.FILE_BASE_URL+othersFilepath);
            }
            else {
                imgOthers.setImageResource(R.drawable.img_placeholder);
            }
        }

        if(accountData.getGSTService().equals(AppConstants.STATUS.YES)){
            RadioButton rb = findViewById(R.id.rb_gst_yes);
            rb.setChecked(true);
            grpGstServices.setVisibility(View.VISIBLE);
            etGstrNum.setText(accountData.getGSTRNo());
            etGstrUserId.setText(accountData.getGSTPortalUserID());
            etGstrPassword.setText(accountData.getGSTPortalPassword());

            String gstRegFilepath = accountData.getGSTRegistrationFilePath();
            Log.d(AppConstants.LOG_TAG, "GSTReg Filepath: " + AppConstants.FILE_BASE_URL+gstRegFilepath);
            if(!TextUtils.isEmpty(gstRegFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+gstRegFilepath)
                        .into(imgGstReg).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgGstReg, AppConstants.FILE_BASE_URL+gstRegFilepath);
            }
            else {
                imgGstReg.setImageResource(R.drawable.img_placeholder);
            }

        }
        else {
            RadioButton rb = findViewById(R.id.rb_gst_no);
            rb.setChecked(true);
            grpGstServices.setVisibility(View.GONE);
        }

        if(accountData.getIncomeTaxService().equals(AppConstants.STATUS.YES)){
            RadioButton rb = findViewById(R.id.rb_it_yes);
            rb.setChecked(true);
            grpITServices.setVisibility(View.VISIBLE);
            etPAN.setText(accountData.getPANNo());
            etTAN.setText(accountData.getTANNo());
            etItrPassword.setText(accountData.getITRPassword());

            String panFilepath = accountData.getPANCardFilePath();
            Log.d(AppConstants.LOG_TAG, "PAN Filepath: " + AppConstants.FILE_BASE_URL+panFilepath);
            if(!TextUtils.isEmpty(panFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+panFilepath)
                        .into(imgPan).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgPan, AppConstants.FILE_BASE_URL+panFilepath);
            }
            else {
                imgPan.setImageResource(R.drawable.img_placeholder);
            }

        }
        else {
            RadioButton rb = findViewById(R.id.rb_it_no);
            rb.setChecked(true);
            grpITServices.setVisibility(View.GONE);
        }

        // CA/ITP Details:
        etCAName.setText(accountData.getNameOfCAITP());
        etCAEmail.setText(accountData.getEmailOfCAITP());
        etCAPhone.setText(accountData.getPhoneOfCAITP());
        etCAAddress.setText(accountData.getAddressOfCAITP());

        // Other Data:
        String auditSheetFilepath = accountData.getPreviousYearAuditSheetFilePath();
        Log.d(AppConstants.LOG_TAG, "AuditSeet Filepath: " + AppConstants.FILE_BASE_URL+auditSheetFilepath);
        if(!TextUtils.isEmpty(auditSheetFilepath)){
            Glide.with(this).load(AppConstants.FILE_BASE_URL+auditSheetFilepath)
                    .into(imgAuditedSheet).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
            setOnClickListenerForImage(imgAuditedSheet, AppConstants.FILE_BASE_URL+auditSheetFilepath);
        }
        else {
            imgAuditedSheet.setImageResource(R.drawable.img_placeholder);
        }


        // Director Details:
        for(DirectorDataObj directorData: accountDetails.getDirectorData()){
            LinearLayout newDirectorLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.card_ad_director_info, null);

            EditText etDirectorFullName = newDirectorLayout.findViewById(R.id.et_director_full_name);
            etDirectorFullName.setText(directorData.getFullName());
            EditText etDirectorEmail = newDirectorLayout.findViewById(R.id.et_director_email);
            etDirectorEmail.setText(directorData.getEmail());
            EditText etDirectorPhone = newDirectorLayout.findViewById(R.id.et_director_phone);
            etDirectorPhone.setText(directorData.getPhone());
            EditText etDirectorAadhar = newDirectorLayout.findViewById(R.id.et_director_aadhar);
            etDirectorAadhar.setText(directorData.getAadhar());
            EditText etDirectorPAN = newDirectorLayout.findViewById(R.id.et_director_pan);
            etDirectorPAN.setText(directorData.getPAN());

            ImageView imgDirectorAadhar = newDirectorLayout.findViewById(R.id.img_director_aadhar_copy);
            String directorAadharFilepath = directorData.getAadharFilePath();
            Log.d(AppConstants.LOG_TAG, "DirectorAadhar Filepath: " + AppConstants.FILE_BASE_URL+directorAadharFilepath);
            if(!TextUtils.isEmpty(directorAadharFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+directorAadharFilepath)
                        .into(imgDirectorAadhar).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgDirectorAadhar, AppConstants.FILE_BASE_URL+directorAadharFilepath);
            }
            else {
                imgDirectorAadhar.setImageResource(R.drawable.img_placeholder);
            }

            ImageView imgDirectorPAN = newDirectorLayout.findViewById(R.id.img_director_pan_copy);
            String directorPANFilepath = directorData.getPANFilePath();
            Log.d(AppConstants.LOG_TAG, "DirectorAadhar Filepath: " + AppConstants.FILE_BASE_URL+directorPANFilepath);
            if(!TextUtils.isEmpty(directorPANFilepath)){
                Glide.with(this).load(AppConstants.FILE_BASE_URL+directorPANFilepath)
                        .into(imgDirectorPAN).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));
                setOnClickListenerForImage(imgDirectorPAN, AppConstants.FILE_BASE_URL+directorPANFilepath);
            }
            else {
                imgDirectorPAN.setImageResource(R.drawable.img_placeholder);
            }

            grpDirectorDetails.addView(newDirectorLayout);

        }

    }

    private void setOnClickListenerForImage(ImageView imageView, final String filepath){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountDetailActivity.this, ImageDisplayActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.IMG_SRC_TYPE, AppConstants.IMAGE_SOURCE.URI);
                intent.putExtra(AppConstants.INTENT_TAG.IMG_URI, filepath);
                startActivity(intent);
            }
        });
    }
}
