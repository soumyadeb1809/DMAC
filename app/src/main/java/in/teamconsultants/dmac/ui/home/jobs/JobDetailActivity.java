package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.teamconsultants.dmac.image.ImageDisplayActivity;
import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.model.JobUploadFile;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;

public class JobDetailActivity extends AppCompatActivity {

    private CustomerJob customerJob;

    private TextView tvFileName, tvFileNotes, tvFileCategory, tvFileType, tvCreatedOn, tvUpdatedOn, tvFileStatus;
    private Button btnReUpload;
    private ImageView imgFilePreview;
    private LinearLayout grpBack;

    private JobUploadFileAdapter jobUploadFileAdapter;
    private ArrayList<JobUploadFile> jobUploadFilesList;
    private String fileStatusStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        grpBack = findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        customerJob = (CustomerJob) intent.getSerializableExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB);
        fileStatusStr = intent.getStringExtra(AppConstants.INTENT_TAG.FILE_STATUS);

        if(customerJob == null){
            Log.d("asdf", "customerJob = null");
            finish();
        }

        initializeUi();


    }

    private void initializeUi() {

        tvFileName = findViewById(R.id.txt_file_name);
        tvFileNotes = findViewById(R.id.txt_file_notes);
        tvFileCategory = findViewById(R.id.txt_file_category);
        tvFileType = findViewById(R.id.txt_file_type);
        tvCreatedOn = findViewById(R.id.txt_created_on);
        tvUpdatedOn = findViewById(R.id.txt_updated_on);
        tvFileStatus = findViewById(R.id.txt_job_status);
        btnReUpload = findViewById(R.id.btn_re_upload_file);
        imgFilePreview = findViewById(R.id.img_file_preview);


        populateUi();

    }


    private void populateUi() {

        if(customerJob.getStatusId().equals(AppConstants.FILE_SEARCH.FAILED_STATUS_ID)){
            btnReUpload.setVisibility(View.VISIBLE);
        }
        else {
            btnReUpload.setVisibility(View.GONE);
        }

        tvFileName.setText(customerJob.getName());
        tvFileNotes.setText(customerJob.getNotes());
        tvFileCategory.setText(customerJob.getFileCategory());
        tvFileType.setText(customerJob.getFileName());

        tvFileStatus.setText(fileStatusStr);

        String statusId = customerJob.getStatusId();

        if(statusId.equals("1")){
            tvFileStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
        }
        else if(statusId.equals("3") || statusId.equals("6") || statusId.equals("10") || statusId.equals("11")){
            tvFileStatus.setBackgroundResource(R.drawable.back_orange_rounded);
        }
        else if(statusId.equals("4") || statusId.equals("7") || statusId.equals("9")){
            tvFileStatus.setBackgroundResource(R.drawable.back_green_rounded);
        }
        else {
            tvFileStatus.setBackgroundResource(R.drawable.back_blue_rounded);
        }

        tvCreatedOn.setText(Utility.getFormattedDate(customerJob.getCreatedAt()));
        tvUpdatedOn.setText(Utility.getFormattedDate(customerJob.getUpdatedAt()));

        if(!TextUtils.isEmpty(customerJob.getFilePath())) {
            Glide.with(this).load(AppConstants.FILE_BASE_URL + customerJob.getFilePath())
                    .into(imgFilePreview).onLoadStarted(getResources().getDrawable(R.drawable.img_placeholder));

            imgFilePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(JobDetailActivity.this, ImageDisplayActivity.class);
                    intent.putExtra(AppConstants.INTENT_TAG.IMG_SRC_TYPE, AppConstants.IMAGE_SOURCE.URI);
                    intent.putExtra(AppConstants.INTENT_TAG.IMG_URI, AppConstants.FILE_BASE_URL + customerJob.getFilePath());
                    startActivity(intent);
                }
            });
        }
        else {
            imgFilePreview.setImageResource(R.drawable.img_placeholder);
        }

        btnReUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobDetailActivity.this, FileReUploadActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB, customerJob);
                startActivity(intent);
            }
        });

    }


}
