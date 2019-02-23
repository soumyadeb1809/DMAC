package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.Notification;

public class NotificationsResponse extends BaseResponse{

    private List<Notification> NotificationList;

    public List<Notification> getNotificationList() {
        return NotificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        NotificationList = notificationList;
    }
}
