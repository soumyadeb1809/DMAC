package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.JobUploadFile;

public class JobUploadFileAdapter extends RecyclerView.Adapter<JobUploadFileAdapter.JobsUploadFileViewHolder>{

    private ArrayList<JobUploadFile> jobUploadFilesList;
    private Context context;


    public class JobsUploadFileViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFileName, tvFileCategory, tvFileType, tvFileNotes, tvFileStatus;
        public ImageView imgUploadedFile;


        public JobsUploadFileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.txt_uploaded_file_name);
            tvFileCategory = itemView.findViewById(R.id.txt_file_category);
            tvFileType = itemView.findViewById(R.id.txt_file_type);
            tvFileStatus = itemView.findViewById(R.id.txt_file_status);
            tvFileNotes = itemView.findViewById(R.id.txt_file_notes);
            imgUploadedFile = itemView.findViewById(R.id.img_uploaded_file);
        }
    }

    public JobUploadFileAdapter(Context context, ArrayList<JobUploadFile> jobUploadFilesList) {
        this.jobUploadFilesList = jobUploadFilesList;
        this.context = context;
    }

    @NonNull
    @Override
    public JobsUploadFileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_job_file_upload, viewGroup,false);
        return new JobUploadFileAdapter.JobsUploadFileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsUploadFileViewHolder jobsUploadFileViewHolder, int position) {
        if(null != jobUploadFilesList.get(position)) {
            JobUploadFile jobUploadFile = jobUploadFilesList.get(position);

            jobsUploadFileViewHolder.tvFileNotes.setText(jobUploadFile.getFileNotes());
            jobsUploadFileViewHolder.tvFileType.setText(jobUploadFile.getFileType());
            jobsUploadFileViewHolder.tvFileStatus.setText(jobUploadFile.getFileStatus());
            jobsUploadFileViewHolder.tvFileCategory.setText(jobUploadFile.getFileCategory());

            Glide.with(context).load(jobUploadFile.getFileUrl()).into(jobsUploadFileViewHolder.imgUploadedFile);

            try {
                String fileUrl = jobUploadFile.getFileUrl();
                String fileName =  fileUrl.substring( fileUrl.lastIndexOf('/')+1, fileUrl.length());
                jobsUploadFileViewHolder.tvFileName.setText(fileName.substring(0, fileName.lastIndexOf('.')));
            }
            catch (Exception e){
                e.printStackTrace();
                jobsUploadFileViewHolder.tvFileName.setText(jobUploadFile.getFileUrl());
            }

        }

    }

    @Override
    public int getItemCount() {
        return jobUploadFilesList.size();
    }


}
