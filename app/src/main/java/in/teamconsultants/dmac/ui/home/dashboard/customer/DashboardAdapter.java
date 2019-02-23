package in.teamconsultants.dmac.ui.home.dashboard.customer;

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
import java.util.List;
import java.util.Date;
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FileCategoryCount;
import in.teamconsultants.dmac.ui.home.jobs.JobDetailActivity;
import in.teamconsultants.dmac.utils.AppConstants;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private List<FileCategoryCount> fileCategoryCountList;
    private Context context;

    public class DashboardViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFileCategory, tvTotalCount, tvBillsEntered;
        public View view;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileCategory = itemView.findViewById(R.id.txt_file_category);
            tvTotalCount = itemView.findViewById(R.id.txt_total);
            tvBillsEntered = itemView.findViewById(R.id.txt_bills_entered);
            view = itemView.findViewById(R.id.extra_space);
        }
    }

    public DashboardAdapter(Context context, List<FileCategoryCount> fileCategoryCountList) {
        this.fileCategoryCountList = fileCategoryCountList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_dashboard_item, viewGroup,false);
        return new DashboardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder dashViewHolder, int position) {

        if(null != fileCategoryCountList.get(position)) {
            FileCategoryCount fileCategoryCount = fileCategoryCountList.get(position);

            if(fileCategoryCount.getTotalCount() != null)
                dashViewHolder.tvTotalCount.setText(fileCategoryCount.getTotalCount());
            else
                dashViewHolder.tvTotalCount.setText("NA");

            dashViewHolder.tvFileCategory.setText(fileCategoryCount.getFileCategory());
        }

        if(position == (fileCategoryCountList.size() - 1)){
            dashViewHolder.view.setVisibility(View.VISIBLE);
        }
        else {
            dashViewHolder.view.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return fileCategoryCountList.size();
    }


}
