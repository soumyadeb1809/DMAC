package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.NotificationType;

public class NotificationTypeResponse extends BaseResponse {

    private List<NotificationType> NotificationTypeList;

    public NotificationTypeResponse() {
    }

    public NotificationTypeResponse(List<NotificationType> notificationTypeList) {
        NotificationTypeList = notificationTypeList;
    }

    public List<NotificationType> getNotificationTypeList() {
        return NotificationTypeList;
    }

    public void setNotificationTypeList(List<NotificationType> notificationTypeList) {
        NotificationTypeList = notificationTypeList;
    }
}
