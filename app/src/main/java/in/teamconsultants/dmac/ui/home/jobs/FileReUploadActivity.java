package in.teamconsultants.dmac.ui.home.jobs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scanlibrary.ScanConstants;
import com.squareup.picasso.Picasso;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.Orientation;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.model.FileImages;
import in.teamconsultants.dmac.network.dto.ReUploadFileResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileReUploadActivity extends AppCompatActivity {

    private EditText etFileId, etFileName, etFileNotes;
    private ImageView imgNewFile;
    private LinearLayout btnUploadFile;
    private CustomerJob customerJob;

    private static final int IMG_REQUEST_CODE = 100;
    private String newFilePath = "";

    private SharedPreferences spUser;
    private String token;
    private ApiInterface apiInterface;
    private Map<String,  String> headerMap;
    private ProgressDialog progress;

    private FileImages fileImages;
    private LinearLayout grpFileImagesContainer;
    private LinearLayout grpBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_re_upload);

        grpBack = findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fileImages = new FileImages();

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        customerJob = (CustomerJob) intent.getSerializableExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB);

        spUser = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = spUser.getString(AppConstants.SP.TAG_TOKEN, "");
        headerMap = new HashMap<>();
        headerMap.put("TOKEN", token);

        initializeUi();

        populateUi();

        setOnClickListeners();
    }

    private void setOnClickListeners() {

        imgNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUserInput();
            }
        });

    }


    private void initializeUi() {

        etFileId = findViewById(R.id.et_file_id);
        etFileName = findViewById(R.id.et_file_name);
        etFileNotes = findViewById(R.id.et_file_notes);
        imgNewFile = findViewById(R.id.img_attach_file);
        btnUploadFile = findViewById(R.id.grp_upload_file);
        grpFileImagesContainer = findViewById(R.id.grp_files_container);

    }


    private void populateUi() {

        etFileId.setText(customerJob.getFId());
        etFileName.setText(customerJob.getFileName());
        etFileNotes.setText(customerJob.getNotes());

    }


    private void verifyUserInput() {

        String notes = etFileNotes.getText().toString();
        if(TextUtils.isEmpty(notes)){
            Utility.showAlert(this, "Info", "File notes cannot be empty");
        }

        if(TextUtils.isEmpty(newFilePath)){
            Utility.showAlert(this, "Info", "Please choose a new file to upload");
        }

        progress.setMessage("Uploading file...");
        progress.show();
        uploadData(notes);

    }

    private void uploadData(String notes) {

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("FileId", Utility.getTextRequestBody(customerJob.getFId()));
        requestBodyMap.put("Name", Utility.getTextRequestBody(customerJob.getName()));
        requestBodyMap.put("Notes", Utility.getTextRequestBody(notes));

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

        MultipartBody.Part multipartImage = Utility.getMultipartImage("UploadFile", destinationPath);

        Call<ReUploadFileResponse> reUploadFileResponseCall = apiInterface.doReUploadFile(headerMap, requestBodyMap, multipartImage);

        reUploadFileResponseCall.enqueue(new Callback<ReUploadFileResponse>() {
            @Override
            public void onResponse(Call<ReUploadFileResponse> call, Response<ReUploadFileResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

                ReUploadFileResponse reUploadFileResponse = response.body();
                if(reUploadFileResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FileReUploadActivity.this);
                    if (reUploadFileResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                        builder.setMessage("File re-uploaded successfully");
                        builder.setTitle("Success");
                        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        if (reUploadFileResponse.getMessage().toLowerCase().contains("token expired")) {
                            Utility.showAlert(FileReUploadActivity.this, "Failed", "Token expired");
                        }
                    }
                }
                else {
                    Utility.forceLogoutUser(FileReUploadActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ReUploadFileResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(FileReUploadActivity.this, "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });


    }


    // Select an image from gallery for the given image position:
    private void selectImage() {
        if(Utility.isPermissionsGranted(FileReUploadActivity.this)) {
           Utility.showImageSelectionDialog(this, IMG_REQUEST_CODE);
        }
        else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage("Please grant the requested permissions when prompted to continue");
            alertBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utility.askForPermissions(FileReUploadActivity.this);
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


            String filePath = Utility.getRealPathFromUri(this, path);

            Log.d(AppConstants.LOG_TAG, "Media Path Received: "+filePath);

            try {

                ArrayList<String> imageUrls = fileImages.getImageUrls();

                if(imageUrls == null)
                    imageUrls = new ArrayList<>();

                imageUrls.add(filePath);

                fileImages.setImageUrls(imageUrls);

                LayoutInflater inflater = getLayoutInflater();
                LinearLayout newFileImage = (LinearLayout) inflater.inflate(R.layout.card_file_image, null);

                ImageView imgFile = newFileImage.findViewById(R.id.img_file);
                TextView tvFilePath = newFileImage.findViewById(R.id.file_name);
                tvFilePath.setText(filePath);

                Picasso.get().load(path).resize(0, 300).into(imgFile);

                grpFileImagesContainer.addView(newFileImage);

                //Picasso.get().load(path).resize(0, 500).into(imgNewFile);

                newFilePath = filePath;

            }
            catch (Exception e){
                Toast.makeText(FileReUploadActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

}
