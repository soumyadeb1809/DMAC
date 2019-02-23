package in.teamconsultants.dmac.ui.home.jobs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scanlibrary.ScanConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.network.dto.CreateJobResponse;
import in.teamconsultants.dmac.model.FileCategoryObj;
import in.teamconsultants.dmac.network.dto.FileCategoryResponse;
import in.teamconsultants.dmac.model.FileTypeObj;
import in.teamconsultants.dmac.network.dto.FileTypeResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.teamconsultants.dmac.utils.AppConstants.INTENT_TAG.CAMSCANNER_INTENT_URI;

public class NewJobActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout grpJobs;
    private LinearLayout grpDefaultJob;
    private LinearLayout btnAddFile;
    private LinearLayout btnUploadJob;

    private ArrayList<LinearLayout> jobsGrpList;
    private Map<Integer, String> jobFileUriMap;

    private ApiInterface apiInterface;
    private Gson gson;

    private String[] fileCategoryArr;

    private ArrayList<FileTypeObj> fileTypeList;
    private HashMap<String, String> fileTypeIdMap;

    private SpinnerAdapter fileCategoryAdapter;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        // Set up toolbar:
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
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");

        progress = new ProgressDialog(this);

        loadCategorySpinnerData();


    }


    private void initializeUi() {

        Log.d(AppConstants.LOG_TAG, "inside");

        grpJobs = findViewById(R.id.grp_jobs);
        grpDefaultJob = findViewById(R.id.grp_default_job);
        btnAddFile = findViewById(R.id.grp_add_job);
        btnUploadJob = findViewById(R.id.grp_upload_job);

        jobsGrpList = new ArrayList<>();

        jobsGrpList.add(grpDefaultJob);

        grpDefaultJob.findViewById(R.id.grp_remove_job).setVisibility(View.GONE);

        fileCategoryAdapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout, fileCategoryArr);

        setListenerForNewJobLayout(grpDefaultJob);

        if(Utility.appInstalledOrNot(this, CAMSCANNER_INTENT_URI)){
            Log.d(AppConstants.LOG_TAG, "CamScanner Installed");
        }
        else {
            Log.d(AppConstants.LOG_TAG, "CamScanner NOT Installed");
        }


        jobFileUriMap = new HashMap<>();

        setOnClickListeners();

    }


    private void setOnClickListeners() {

        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jobsGrpList.size() < AppConstants.MAX_ALLOWED_JOBS) {
                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout newJobLayout = (LinearLayout) inflater.inflate(R.layout.card_new_job, null);
                    grpJobs.addView(newJobLayout);
                    jobsGrpList.add(newJobLayout);
                    setListenerForNewJobLayout(newJobLayout);

                    if(jobsGrpList.size() == AppConstants.MAX_ALLOWED_JOBS){
                        btnAddFile.setVisibility(View.INVISIBLE);
                    }
                    else {
                        btnAddFile.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(NewJobActivity.this, "Max allowed job upload limit is "+AppConstants.MAX_ALLOWED_JOBS, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUploadJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpload(v);
            }
        });

    }


    private void setListenerForNewJobLayout(final LinearLayout newJobLayout) {

        final Spinner spinnerFileType = newJobLayout.findViewById(R.id.spinner_file_type);
        //spinnerFileType.setAdapter(fileTypeAdapter);


        final Spinner spinnerFileCategory = newJobLayout.findViewById(R.id.spinner_file_category);
        spinnerFileCategory.setAdapter(fileCategoryAdapter);

        spinnerFileCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] typeArray = prepareTypeArray(position);
                SpinnerAdapter fileTypeAdapter = new SimpleSpinnerAdapter(NewJobActivity.this, R.layout.spinner_layout, typeArray);
                spinnerFileType.setAdapter(fileTypeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(fileCategoryArr.length > 0){
                    spinnerFileCategory.setSelection(0);
                }
            }
        });


        if(newJobLayout.getId() != R.id.grp_default_job) {

            LinearLayout grpRemoveJobCard = newJobLayout.findViewById(R.id.grp_remove_job);
            grpRemoveJobCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grpJobs.removeView(newJobLayout);
                    int position = jobsGrpList.indexOf(newJobLayout);
                    jobFileUriMap.remove(position);
                    jobsGrpList.remove(newJobLayout);
                }
            });

            Log.d(AppConstants.LOG_TAG, "List Size: " + jobsGrpList.size());
        }

        ImageView imgAttachFile = newJobLayout.findViewById(R.id.img_attach_file);

        final int cardPosition = jobsGrpList.indexOf(newJobLayout);

        imgAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppConstants.LOG_TAG, "Image clicked at: " + cardPosition);
                selectImage(cardPosition);
            }
        });

    }

    private String[] prepareTypeArray(int position) {
        String category = fileCategoryArr[position];
        ArrayList<FileTypeObj> selectedCategoryTypes = new ArrayList<>();
        for (FileTypeObj fileTypeObj : fileTypeList){
            if (fileTypeObj.getFileCategory().equals(category)){
                selectedCategoryTypes.add(fileTypeObj);
            }
        }
        String[] typeArray = new String[selectedCategoryTypes.size()];
        for (int i = 0; i < selectedCategoryTypes.size(); i++){
            typeArray[i] = selectedCategoryTypes.get(i).getFileName();
        }

        return typeArray;
    }

    private void selectImage(int cardPosition) {
        Log.d(AppConstants.LOG_TAG, "Permission: " + Utility.isPermissionsGranted(NewJobActivity.this));
        if(Utility.isPermissionsGranted(NewJobActivity.this)) {
            /*Intent intent = new Intent();
            intent.putExtra("position", cardPosition);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, cardPosition);*/
           /* int preference = ScanConstants.OPEN_MEDIA;
            Intent intent = new Intent(NewJobActivity.this, ScanActivity.class);
            intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
            startActivityForResult(intent, cardPosition);*/
            Utility.showImageSelectionDialog(this, cardPosition);

        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Please grant the requested permissions when prompted to continue");
            alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utility.askForPermissions(NewJobActivity.this);
                }
            });
            alertBuilder.setCancelable(false);
            alertBuilder.show();
        }

    }



    private void loadCategorySpinnerData() {

        progress.setMessage("Loading data...");
        progress.setCancelable(false);
        progress.show();

        /*final Map<String, String> headers = new HashMap<>();
        headers.put("TOKEN", "800c47de571bf0f49c80369ba34c520a");*/

        Call<FileCategoryResponse> fileCategoryResponseCall = apiInterface.doGetFileCategory(Utility.getHeader(token));

        fileCategoryResponseCall.enqueue(new Callback<FileCategoryResponse>() {
            @Override
            public void onResponse(Call<FileCategoryResponse> call, Response<FileCategoryResponse> response) {
                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                FileCategoryResponse fileCategoryResponse = response.body();

                if(fileCategoryResponse.getFileCategoryList() != null) {
                    prepareFileCategoryArray(fileCategoryResponse.getFileCategoryList());
                }
                else {
                    progress.dismiss();
                    Log.d(AppConstants.LOG_TAG, "TOKEN EXPIRED!");
                }

            }

            @Override
            public void onFailure(Call<FileCategoryResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                Utility.showAlert(NewJobActivity.this, "Error", "Something went wrong, please try again");
            }
        });

    }

    private void prepareFileCategoryArray(ArrayList<FileCategoryObj> fileCategoryList) {
        fileCategoryArr = new String[fileCategoryList.size()];
        for (int i = 0; i < fileCategoryList.size(); i++){
            fileCategoryArr[i] = fileCategoryList.get(i).getFileCategory();
        }

        loadTypeSpinnerData();

    }

    private void loadTypeSpinnerData() {

       /* final Map<String, String> headers = new HashMap<>();
        headers.put("TOKEN", "800c47de571bf0f49c80369ba34c520a");*/

        Call<FileTypeResponse> fileTypeResponseCall = apiInterface.doGetFileType(Utility.getHeader(token), "");
        fileTypeResponseCall.enqueue(new Callback<FileTypeResponse>() {
            @Override
            public void onResponse(Call<FileTypeResponse> call, Response<FileTypeResponse> response) {

                FileTypeResponse fileTypeResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(fileTypeResponse));
                if(fileTypeResponse.getFileTypeList() != null) {
                    fileTypeList = fileTypeResponse.getFileTypeList();

                    fileTypeIdMap = new HashMap<>();
                    for(FileTypeObj fileTypeObj: fileTypeList){
                        fileTypeIdMap.put(fileTypeObj.getFileName(), fileTypeObj.getFTId());
                    }

                    initializeUi();
                }
                else {
                    Log.d(AppConstants.LOG_TAG, "TOKEN EXPIRED!");
                }
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<FileTypeResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                Utility.showAlert(NewJobActivity.this, "Error", "Something went wrong, please try again");
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){

            //Uri path = data.getData();
            Uri path = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);

            /*String wholeID = DocumentsContract.getDocumentId(path);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = { MediaStore.Images.Media.DATA };

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{ id }, null);*/

            String filePath = "";

            filePath = Utility.getRealPathFromUri(this, path);

            /*int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
*/
            Log.d(AppConstants.LOG_TAG, "Media Path Received: "+filePath);

            jobFileUriMap.put(requestCode, filePath);

            try {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

                setBitmapToImgAttachFile(requestCode, path);

            }
            catch (Exception e){
                Toast.makeText(NewJobActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    private void setBitmapToImgAttachFile(int position, Uri filepath) {

        LinearLayout grpNewJob = jobsGrpList.get(position);

        ImageView imgAttachFile = grpNewJob.findViewById(R.id.img_attach_file);

        //Picasso.get().load(Uri.parse(filepath)).resize(0, 500).into(imgAttachFile);
        Picasso.get().load(filepath).resize(0, 500).into(imgAttachFile);
        //imgAttachFile.setImageBitmap(bitmap);

    }

    private void startUpload(View v) {


        HashMap<String, RequestBody> partMap = new HashMap<>();

        ArrayList<MultipartBody.Part> fileMPList = new ArrayList<>();

        for (int i = 0; i < jobsGrpList.size(); i++) {

            LinearLayout newJob = jobsGrpList.get(i);

            EditText etNotes = newJob.findViewById(R.id.et_notes);
            String jobNotes = etNotes.getText().toString();

            if(TextUtils.isEmpty(jobNotes)){
                Utility.showAlert(NewJobActivity.this, "Info", "Please enter notes for all Files");
                return;
            }

            partMap.put("Notes["+i+"]", getTextRequestBody(jobNotes));

            Spinner fileTypeSpinner = newJob.findViewById(R.id.spinner_file_type);
            String fileType = fileTypeSpinner.getSelectedItem().toString();
            partMap.put("FileType["+i+"]", getTextRequestBody(fileTypeIdMap.get(fileType)));

            Spinner fileCategorySpinner = newJob.findViewById(R.id.spinner_file_category);
            String fileCategory = fileCategorySpinner.getSelectedItem().toString();
            partMap.put("FileCategory["+i+"]", getTextRequestBody(fileCategory));


            String filePath = jobFileUriMap.get(i);
            //Log.d(AppConstants.LOG_TAG, "File Uri: "+filePath);
            Log.d(AppConstants.LOG_TAG, "File Path: " + filePath);
            if (filePath != null) {
                File file = new File(filePath);
                Log.d(AppConstants.LOG_TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("JobFile["+i+"]", file.getName(), mFile);
                fileMPList.add(fileToUpload);
                //multipartBodyMap.put("JobFile[]", fileToUpload);

            }

        }

        progress.setMessage("Uploading job...");
        progress.setCancelable(false);
        progress.show();

      /*  final Map<String, String> headers = new HashMap<>();
        headers.put("TOKEN", "2e98b6149d2bc958410f017ca036c81e");*/


        Call<CreateJobResponse> apiCall = apiInterface.doCreateJob(Utility.getHeader(token), partMap, fileMPList);

        apiCall.enqueue(new Callback<CreateJobResponse>() {

            @Override
            public void onResponse(Call<CreateJobResponse> call, Response<CreateJobResponse> response) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                CreateJobResponse createJobResponse = response.body();
                if(createJobResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NewJobActivity.this);
                    alertBuilder.setTitle("Success");
                    alertBuilder.setMessage("File(s) uploaded successfully");
                    alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertBuilder.show();

                }
                else {
                    Utility.showAlert(NewJobActivity.this, "Error", "Something went wrong, please try again");
                }
            }

            @Override
            public void onFailure(Call<CreateJobResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
                Log.d(AppConstants.LOG_TAG, "Call: "+call.request().toString());
                Log.d(AppConstants.LOG_TAG, "Body: "+gson.toJson(call.request().body()));
                Utility.showAlert(NewJobActivity.this, "Error", "Something went wrong, please try again");
            }
        });



    }
    public RequestBody getTextRequestBody(String text){
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }



}
