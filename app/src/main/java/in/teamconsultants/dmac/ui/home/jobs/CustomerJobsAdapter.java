package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.utils.AppConstants;

public class CustomerJobsAdapter  extends RecyclerView.Adapter<CustomerJobsAdapter.JobsViewHolder> {

    private ArrayList<CustomerJob> customerJobsList;
    private Context context;
    private HashMap<String, String> statusMap;

    public class JobsViewHolder extends RecyclerView.ViewHolder{

        public TextView tvFileName, tvCreateDate, tvFileStatus, tvFileNotes, tvFileId;
        public LinearLayout contentView;
        public View extraSpace;
        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.grp_content);
            tvFileName = itemView.findViewById(R.id.txt_file_name);
            tvCreateDate = itemView.findViewById(R.id.txt_date);
            tvFileStatus = itemView.findViewById(R.id.txt_job_status);
            extraSpace = itemView.findViewById(R.id.extra_space);
            tvFileNotes = itemView.findViewById(R.id.txt_file_notes);
            tvFileId = itemView.findViewById(R.id.txt_file_id);
        }
    }

    public CustomerJobsAdapter(Context context, ArrayList<CustomerJob> customerJobsList, HashMap<String, String> statusMap) {
        this.customerJobsList = customerJobsList;
        this.context = context;
        this.statusMap = statusMap;
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_customer_job, viewGroup,false);
        return new JobsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder jobsViewHolder, int position) {

        if(null != customerJobsList.get(position)) {
            final CustomerJob customerJob = customerJobsList.get(position);
            jobsViewHolder.tvFileName.setText(customerJob.getName());
            jobsViewHolder.tvFileStatus.setText(statusMap.get(customerJob.getStatusId()));
            String statusId = customerJob.getStatusId();

            if(statusId.equals("1")){
                jobsViewHolder.tvFileStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
            }
            else if(statusId.equals("3") || statusId.equals("6") || statusId.equals("10") || statusId.equals("11")){
                jobsViewHolder.tvFileStatus.setBackgroundResource(R.drawable.back_orange_rounded);
            }
            else if(statusId.equals("4") || statusId.equals("7") || statusId.equals("9")){
                jobsViewHolder.tvFileStatus.setBackgroundResource(R.drawable.back_green_rounded);
            }
            else {
                jobsViewHolder.tvFileStatus.setBackgroundResource(R.drawable.back_blue_rounded);
            }

            jobsViewHolder.tvFileNotes.setText(customerJob.getNotes());
            String createDate = customerJob.getCreatedAt();
            String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
            String finalDate = createDate;
            try{
                Date date = simpleDateFormat.parse(createDate);
                String datePattern = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                finalDate = dateFormat.format(date);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            jobsViewHolder.tvFileId.setText("#"+customerJob.getFId());

            jobsViewHolder.tvCreateDate.setText(finalDate);


            jobsViewHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB, customerJob);
                    intent.putExtra(AppConstants.INTENT_TAG.FILE_STATUS, statusMap.get(customerJob.getStatusId()));
                    context.startActivity(intent);
                }
            });

           if (position == customerJobsList.size() - 1){
                jobsViewHolder.extraSpace.setVisibility(View.VISIBLE);
            }
            else {
               jobsViewHolder.extraSpace.setVisibility(View.GONE);
           }



        }

    }

    @Override
    public int getItemCount() {
        return customerJobsList.size();
    }


}
