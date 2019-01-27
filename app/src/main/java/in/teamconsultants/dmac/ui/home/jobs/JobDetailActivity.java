package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.model.JobUploadFile;
import in.teamconsultants.dmac.utils.AppConstants;

public class JobDetailActivity extends AppCompatActivity {

    private CustomerJob customerJob;

    private TextView tvJobName, tvAccountName, tvEndCustomerName, tvCreatedOn, tvUpdatedOn, tvJobStatus;
    private LinearLayout grpUploadedFiles;
    private RecyclerView rvUploadedFiles;

    private JobUploadFileAdapter jobUploadFileAdapter;
    private ArrayList<JobUploadFile> jobUploadFilesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        customerJob = (CustomerJob) intent.getSerializableExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB);

        if(customerJob == null){
            Log.d("asdf", "customerJob = null");
            finish();
        }

        initializeUi();

        populateUi();
    }

    private void initializeUi() {

        tvJobName = findViewById(R.id.txt_file_name);
        tvAccountName = findViewById(R.id.txt_account_name);
        tvEndCustomerName = findViewById(R.id.txt_customer_name);
        tvCreatedOn = findViewById(R.id.txt_created_on);
        tvUpdatedOn = findViewById(R.id.txt_updated_on);
        tvJobStatus = findViewById(R.id.txt_job_status);
        grpUploadedFiles = findViewById(R.id.grp_uploaded_files);

        rvUploadedFiles = findViewById(R.id.rv_job_file_upload);

        jobUploadFilesList = new ArrayList<>();

    }


    private void populateUi() {

      /*  tvFileName.setText(customerJob.getJobName());
        tvAccountName.setText(customerJob.getAccountName());
        tvEndCustomerName.setText(customerJob.getEndCustomer());
        tvCreatedOn.setText(getFormattedDate(customerJob.getCreateDate()));
        tvUpdatedOn.setText(getFormattedDate(customerJob.getUpdateDate()));

        tvFileStatus.setText(customerJob.getJobStatus());

        if(null != customerJob.getJobUploadFiles() && customerJob.getJobUploadFiles().size() > 0){
            jobUploadFilesList = customerJob.getJobUploadFiles();
            jobUploadFileAdapter = new JobUploadFileAdapter(this, jobUploadFilesList);
            rvUploadedFiles.setLayoutManager(new LinearLayoutManager(this));
            rvUploadedFiles.setAdapter(jobUploadFileAdapter);
            rvUploadedFiles.setHasFixedSize(false);
        }
        else {
            grpUploadedFiles.setVisibility(View.GONE);
        }*/

    }

    public String getFormattedDate(String dateTime){
        String result = dateTime;
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
        try{
            Date date = simpleDateFormat.parse(dateTime);
            String datePattern = "HH:mm, dd MMM yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            result = dateFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
