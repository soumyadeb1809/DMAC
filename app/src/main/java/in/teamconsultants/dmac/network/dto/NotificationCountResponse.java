package in.teamconsultants.dmac.network.dto;

public class NotificationCountResponse extends BaseResponse {

    private String TotalNotification;
    private String TotalUnreadNotification;

    public String getTotalNotification() {
        return TotalNotification;
    }

    public void setTotalNotification(String totalNotification) {
        TotalNotification = totalNotification;
    }

    public String getTotalUnreadNotification() {
        return TotalUnreadNotification;
    }

    public void setTotalUnreadNotification(String totalUnreadNotification) {
        TotalUnreadNotification = totalUnreadNotification;
    }

}
