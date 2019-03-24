package in.teamconsultants.dmac.ui.notification;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Notification;
import in.teamconsultants.dmac.model.NotificationType;
import in.teamconsultants.dmac.model.User;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.NotificationTypeResponse;
import in.teamconsultants.dmac.network.dto.NotificationsResponse;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Spinner spinNotfType;
    private RecyclerView rvNotifications;
    private LinearLayout grpBack;

    private List<NotificationType> notificationTypeList;
    private Map<String, String> notificationTypeMap;
    private String[] notificationTypeArr;

    private List<Notification> notificationList;
    private Map<String, List<Notification>> notiStringListMap;

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private SimpleSpinnerAdapter fileStatusSpinnerAdapter;

    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        grpBack = findViewById(R.id.grp_back);
        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(this);

        fetchNotificationTypes();

    }

    private void fetchNotificationTypes() {
        progress.setMessage("Fetching notifications...");
        progress.setCancelable(false);
        progress.show();

        Call<NotificationTypeResponse> notificationTypeCall = apiInterface.doGetNotificationsTypes(token);
        notificationTypeCall.enqueue(new Callback<NotificationTypeResponse>() {
            @Override
            public void onResponse(Call<NotificationTypeResponse> call, Response<NotificationTypeResponse> response) {

                if(response.body() == null)
                    return;

                NotificationTypeResponse notificationTypeResponse = response.body();
                if(notificationTypeResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    notificationTypeList = notificationTypeResponse.getNotificationTypeList();
                    
                    notificationTypeArr = new String[notificationTypeList.size() + 1];
                    notificationTypeArr[0] = "All Notifications";
                    
                    for (int i = 0; i < notificationTypeList.size(); i++){
                        NotificationType notificationType = notificationTypeList.get(i);
                        notificationTypeArr[i+1] = notificationType.getNt_type_name();
                    }
                    
                    setUpSpinner(notificationTypeArr);
                    
                    fetchNotificationsData();
                    
                }
                else {
                    //Utility.showAlert(NotificationActivity.this, "Failed", "Something went wrong, please try again");
                    Utility.forceLogoutUser(NotificationActivity.this);
                }
            }

            @Override
            public void onFailure(Call<NotificationTypeResponse> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Utility.showAlert(NotificationActivity.this, "Failed", "Something went wrong, please try again");
            }
        });

    }

    private void fetchNotificationsData() {

    }

    private void setUpSpinner(String[] dataArr) {

        spinNotfType = findViewById(R.id.spinner_notification_type);
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(this, R.layout.spinner_layout, dataArr);
        spinNotfType.setAdapter(adapter);

        fetchNotifications();
    }

    private void fetchNotifications() {

        Call<NotificationsResponse> notificationsCall = apiInterface.doGetNotificationList(token, "");
        notificationsCall.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                progress.dismiss();

                if(response.body() == null)
                    return;

                NotificationsResponse notificationsResponse = response.body();
                if(notificationsResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    notificationList = notificationsResponse.getNotificationList();
                    notiStringListMap = new HashMap<>();
                    notiStringListMap.put("all", notificationList);
                    for(int i = 0; i < notificationList.size(); i++){

                        Notification notification = notificationList.get(i);
                        List<Notification> typeList = notiStringListMap.get(notification.getNotification_type_id());
                        if(typeList == null){
                            typeList = new ArrayList<>();
                        }

                        typeList.add(notification);

                        notiStringListMap.put(notification.getNotification_type_id(), typeList);

                    }

                    setUpNotificationsList();
                }
                else {
                    //Utility.showAlert(NotificationActivity.this, "Failed", "Something went wrong, please try again");
                    Utility.forceLogoutUser(NotificationActivity.this);
                }

            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Utility.showAlert(NotificationActivity.this, "Failed", "Something went wrong, please try again");
            }
        });

    }

    private void setUpNotificationsList() {
        rvNotifications = findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setNestedScrollingEnabled(false);
        refreshRecyclerView();
        spinNotfType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private void refreshRecyclerView() {
        int spinnerPos = spinNotfType.getSelectedItemPosition();
        NotificationType selectedType = (spinnerPos > 0) ? notificationTypeList.get(spinnerPos-1) : null;
        List<Notification> requiredNotifList;
        if(selectedType == null){
            requiredNotifList = notiStringListMap.get("all");
        }
        else {
            requiredNotifList = notiStringListMap.get(selectedType.getNt_id());
        }

        NotificationAdapter notificationAdapter = new NotificationAdapter(this, requiredNotifList);
        rvNotifications.setAdapter(notificationAdapter);
    }
}
