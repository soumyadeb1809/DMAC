package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.utils.AppConstants;

public class CustomerJobsAdapter  extends RecyclerView.Adapter<CustomerJobsAdapter.JobsViewHolder> {

    private ArrayList<CustomerJob> customerJobsList;
    private Context context;

    public class JobsViewHolder extends RecyclerView.ViewHolder{

        public TextView tvJobName, tvCreateDate, tvJobStatus;
        public View view;
        public JobsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvJobName = itemView.findViewById(R.id.txt_job_name);
            tvCreateDate = itemView.findViewById(R.id.txt_date);
            tvJobStatus = itemView.findViewById(R.id.txt_job_status);
        }
    }

    public CustomerJobsAdapter(Context context, ArrayList<CustomerJob> customerJobsList) {
        this.customerJobsList = customerJobsList;
        this.context = context;
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
            jobsViewHolder.tvJobName.setText(customerJob.getJobName());
            jobsViewHolder.tvJobStatus.setText(customerJob.getJobStatus());
            String createDate = customerJob.getCreateDate();
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

            jobsViewHolder.tvCreateDate.setText(finalDate);

            jobsViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra(AppConstants.INTENT_TAG.CUSTOMER_JOB, customerJob);
                    context.startActivity(intent);
                }
            });

            if (position == customerJobsList.size() - 1){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(40, 40, 40, 200);
                jobsViewHolder.view.setLayoutParams(params);
            }
        }

    }

    @Override
    public int getItemCount() {
        return customerJobsList.size();
    }


}
