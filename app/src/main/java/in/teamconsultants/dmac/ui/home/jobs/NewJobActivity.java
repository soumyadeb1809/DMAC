package in.teamconsultants.dmac.ui.home.jobs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scanlibrary.ScanConstants;
import com.squareup.picasso.Picasso;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.Orientation;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FileImages;
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

public class NewJobActivity extends AppCompatActivity {

    private LinearLayout grpJobs;
    private LinearLayout grpDefaultJob;
    private LinearLayout btnAddFile;
    private LinearLayout btnUploadJob;

    private LinearLayout grpBack;

    private ArrayList<LinearLayout> jobsGrpList;
    private Map<Integer, String> jobFileUriMap;

    private Map<Integer, FileImages> fileImagesUriMap;

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

        // Set up back button click:
        grpBack = findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
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


        jobFileUriMap = new HashMap<>();
        fileImagesUriMap = new HashMap<>();

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

                if(response.body() == null)
                    return;

                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                FileCategoryResponse fileCategoryResponse = response.body();

                if(fileCategoryResponse.getFileCategoryList() != null) {
                    prepareFileCategoryArray(fileCategoryResponse.getFileCategoryList());
                }
                else {
                    progress.dismiss();
                    Utility.forceLogoutUser(NewJobActivity.this);
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

        Call<FileTypeResponse> fileTypeResponseCall = apiInterface.doGetFileType(Utility.getHeader(token), "");
        fileTypeResponseCall.enqueue(new Callback<FileTypeResponse>() {
            @Override
            public void onResponse(Call<FileTypeResponse> call, Response<FileTypeResponse> response) {

                if(response.body() == null)
                    return;

                FileTypeResponse fileTypeResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: " + gson.toJson(fileTypeResponse));

                if (fileTypeResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    if (fileTypeResponse.getFileTypeList() != null) {
                        fileTypeList = fileTypeResponse.getFileTypeList();

                        fileTypeIdMap = new HashMap<>();
                        for (FileTypeObj fileTypeObj : fileTypeList) {
                            fileTypeIdMap.put(fileTypeObj.getFileName(), fileTypeObj.getFTId());
                        }

                        initializeUi();
                    } else {
                        Log.d(AppConstants.LOG_TAG, "TOKEN EXPIRED!");
                    }
                    progress.dismiss();
                }
                else {
                    Utility.forceLogoutUser(NewJobActivity.this);
                }
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

            String filePath = Utility.getRealPathFromUri(this, path);

            Log.d(AppConstants.LOG_TAG, "Media Path Received: "+filePath);

            jobFileUriMap.put(requestCode, filePath);

            FileImages fileImages = fileImagesUriMap.get(requestCode);

            if(fileImages == null)
                fileImages = new FileImages();


            ArrayList<String> fileImageUrls = fileImages.getImageUrls();
            if(fileImageUrls == null)
                fileImageUrls = new ArrayList<>();

            fileImageUrls.add(filePath);

            fileImages.setImageUrls(fileImageUrls);

            fileImagesUriMap.put(requestCode, fileImages);


            try {

                setBitmapToImgAttachFile(requestCode, path, filePath);

            }
            catch (Exception e){
                Toast.makeText(NewJobActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    private void setBitmapToImgAttachFile(int position, Uri path, String filePath) {

        LinearLayout grpNewJob = jobsGrpList.get(position);

        //ImageView imgAttachFile = grpNewJob.findViewById(R.id.img_attach_file);

        LinearLayout grpImageContainer = grpNewJob.findViewById(R.id.grp_files_container);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout newFileImage = (LinearLayout) inflater.inflate(R.layout.card_file_image, null);

        ImageView imgFile = newFileImage.findViewById(R.id.img_file);
        TextView tvFilePath = newFileImage.findViewById(R.id.file_name);

        tvFilePath.setText(filePath);

        //Picasso.get().load(Uri.parse(filepath)).resize(0, 500).into(imgAttachFile);
        Picasso.get().load(path).resize(0, 300).into(imgFile);

        grpImageContainer.addView(newFileImage);

    }

    private void startUpload(View v) {

        HashMap<String, RequestBody> partMap = new HashMap<>();

        ArrayList<MultipartBody.Part> fileMPList = new ArrayList<>();

        for (int i = 0; i < jobsGrpList.size(); i++) {

            LinearLayout newJob = jobsGrpList.get(i);

            EditText etNotes = newJob.findViewById(R.id.et_notes);
            String jobNotes = etNotes.getText().toString();

            /*if(TextUtils.isEmpty(jobNotes)){
                Utility.showAlert(NewJobActivity.this, "Info", "Please enter notes for all Files");
                return;
            }*/

            partMap.put("Notes["+i+"]", getTextRequestBody(jobNotes));

            Spinner fileTypeSpinner = newJob.findViewById(R.id.spinner_file_type);
            String fileType = fileTypeSpinner.getSelectedItem().toString();
            partMap.put("FileType["+i+"]", getTextRequestBody(fileTypeIdMap.get(fileType)));

            Spinner fileCategorySpinner = newJob.findViewById(R.id.spinner_file_category);
            String fileCategory = fileCategorySpinner.getSelectedItem().toString();
            partMap.put("FileCategory["+i+"]", getTextRequestBody(fileCategory));

            progress.setMessage("Uploading files...");
            progress.setCancelable(false);
            progress.show();

            String filePath = jobFileUriMap.get(i);

            FileImages fileImages = fileImagesUriMap.get(i);

            ArrayList<String> fileImagesPathList = fileImages.getImageUrls();

            Bitmap[] bitmapParts = new Bitmap[fileImagesPathList.size()];

            for(int j = 0; j < fileImagesPathList.size(); j++){
                String fileImagePath = fileImagesPathList.get(j);
                bitmapParts[j] = BitmapFactory.decodeFile(fileImagePath);
            }

            TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();

            options.compressionScheme = CompressionScheme.JPEG;
            //By default orientation is top left
            options.orientation = Orientation.LEFT_TOP;
            //Add author tag to output file
            options.author = "beyka";
            //Add copyright tag to output file
            options.copyright = "Some copyright";
            //Save image as tif. If image saved succesfull true will be returned

            String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());

            //String destinationPath = "/sdcard/"+fileName+".tif";
            String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dmac/files/";
            File js = new File(destinationPath);
            if(!js.exists()){
                js.mkdirs();
            }

            destinationPath = destinationPath +fileName+".tif";

            boolean saved = TiffSaver.saveBitmap(destinationPath, bitmapParts[0], options);

            if(bitmapParts.length > 1) {

                for (int k = 1; k < bitmapParts.length; k++) {
                    TiffSaver.appendBitmap(destinationPath, bitmapParts[k], options);
                }
            }

            Log.d(AppConstants.LOG_TAG, "File Path: " + filePath);
            if (filePath != null) {
                File file = new File(destinationPath);

                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("JobFile["+i+"]", file.getName(), mFile);
                fileMPList.add(fileToUpload);

            }

        }

        Call<CreateJobResponse> apiCall = apiInterface.doCreateJob(Utility.getHeader(token), partMap, fileMPList);

        apiCall.enqueue(new Callback<CreateJobResponse>() {

            @Override
            public void onResponse(Call<CreateJobResponse> call, Response<CreateJobResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

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
                    Utility.forceLogoutUser(NewJobActivity.this);
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
