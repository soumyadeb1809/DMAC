package in.teamconsultants.dmac.ui.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private Context context;

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        
        public TextView tvNotificationTitle, tvDate;
        
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.txt_notif_text);
            tvDate = itemView.findViewById(R.id.txt_date);
        }
    }

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_notification, viewGroup,false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder viewHolder, int position) {

        if(null != notificationList.get(position)) {
            Notification notification = notificationList.get(position);
            viewHolder.tvNotificationTitle.setText(notification.getMessage());

            String createDate = notification.getCreated_at();
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

            viewHolder.tvDate.setText(finalDate);

        }

       /* if(position == (notificationList.size() - 1)){
            viewHolder.view.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.view.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }


}
