package in.teamconsultants.dmac.ui.home.reports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Report;
import in.teamconsultants.dmac.ui.home.jobs.JobDetailActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.DownloadUtils;
import in.teamconsultants.dmac.utils.PermissionUtils;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {

    private ArrayList<Report> reportsList;
    private Activity activity;

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

    public ReportsAdapter(Activity activity, ArrayList<Report> reportsList) {
        this.reportsList = reportsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_report, viewGroup,false);
        return new ReportsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder viewHolder, int position) {

        if(null != reportsList.get(position)) {
            final Report report = reportsList.get(position);
            viewHolder.tvReportType.setText(report.getRt_name());
            viewHolder.tvStatus.setText(report.getStatus());

            if(report.getStatus().equals("Uploaded")){
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_green_rounded);
            }
            else {
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
            }
            
            String statusId = report.getStatus_id();

           /* if(statusId.equals("1")){
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
            }
            else if(statusId.equals("3") || statusId.equals("6") || statusId.equals("10") || statusId.equals("11")){
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_orange_rounded);
            }
            else if(statusId.equals("4") || statusId.equals("7") || statusId.equals("9")){
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_green_rounded);
            }
            else {
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_blue_rounded);
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

            viewHolder.tvDateRange.setText(finalDate);

            final String fileTitle = report.getRt_name().replace(" ", "_") + "_"
                    + report.getReport_id() + "_" + finalDate.replace(" ", "_");

            String downloadPath = AppConstants.FILE_BASE_URL + report.getReport_path();
            final Uri downloadUri = Uri.parse(downloadPath);

            viewHolder.grpDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PermissionUtils.checkStoragePermissions(activity)) {
                        if (null == report.getReport_path() || TextUtils.isEmpty(report.getReport_path()) || null == downloadUri) {
                            Toast.makeText(activity, "No file available for download", Toast.LENGTH_SHORT).show();
                        } else {
                            DownloadUtils.downloadFile(activity, downloadUri, fileTitle);
                        }
                    }
                    else {
                        PermissionUtils.askForStoragePermissions(activity);
                    }
                }
            });

            if(report.getStatus().equals("Uploaded")){
                viewHolder.grpDownload.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.grpDownload.setVisibility(View.GONE);
            }


        }

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }


}
