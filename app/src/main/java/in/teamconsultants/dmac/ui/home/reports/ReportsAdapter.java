package in.teamconsultants.dmac.ui.home.reports;

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
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Report;
import in.teamconsultants.dmac.ui.home.jobs.JobDetailActivity;
import in.teamconsultants.dmac.utils.AppConstants;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {

    private ArrayList<Report> reportsList;
    private Context context;

    public class ReportsViewHolder extends RecyclerView.ViewHolder{

        public TextView tvReportType, tvDateRange, tvStatus;
        public LinearLayout grpDownload;
        public View extraSpace;
        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportType = itemView.findViewById(R.id.txt_report_type);
            tvDateRange = itemView.findViewById(R.id.txt_date);
            tvStatus = itemView.findViewById(R.id.txt_status);
            extraSpace = itemView.findViewById(R.id.extra_space);
            grpDownload = itemView.findViewById(R.id.grp_download_report);
        }
    }

    public ReportsAdapter(Context context, ArrayList<Report> reportsList) {
        this.reportsList = reportsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_report, viewGroup,false);
        return new ReportsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder jobsViewHolder, int position) {

        if(null != reportsList.get(position)) {
            final Report report = reportsList.get(position);
            jobsViewHolder.tvReportType.setText(report.getRt_name());
            jobsViewHolder.tvStatus.setText(report.getStatus());
            String statusId = report.getStatus_id();

           /* if(statusId.equals("1")){
                jobsViewHolder.tvStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
            }
            else if(statusId.equals("3") || statusId.equals("6") || statusId.equals("10") || statusId.equals("11")){
                jobsViewHolder.tvStatus.setBackgroundResource(R.drawable.back_orange_rounded);
            }
            else if(statusId.equals("4") || statusId.equals("7") || statusId.equals("9")){
                jobsViewHolder.tvStatus.setBackgroundResource(R.drawable.back_green_rounded);
            }
            else {
                jobsViewHolder.tvStatus.setBackgroundResource(R.drawable.back_blue_rounded);
            }*/
           
            String startDate = report.getReport_from();
            String endDate = report.getReport_to();

            String dateTimeFormat = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
            String finalDate = startDate + " to " + endDate;
            try{
                Date fromDate = simpleDateFormat.parse(startDate);
                Date toDate = simpleDateFormat.parse(endDate);
                String datePattern = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                finalDate = dateFormat.format(fromDate) +" to "+ dateFormat.format(toDate);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            jobsViewHolder.tvDateRange.setText(finalDate);

        }

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }


}
