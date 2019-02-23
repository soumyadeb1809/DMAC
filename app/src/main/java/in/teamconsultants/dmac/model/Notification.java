package in.teamconsultants.dmac.model;

public class Notification {

    private String notification_id;
    private String message;
    private String notification_type_id;
    private String created_at;
    private String created_by;
    private String nt_id;
    private String nt_type_name;


    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_type_id() {
        return notification_type_id;
    }

    public void setNotification_type_id(String notification_type_id) {
        this.notification_type_id = notification_type_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getNt_id() {
        return nt_id;
    }

    public void setNt_id(String nt_id) {
        this.nt_id = nt_id;
    }

    public String getNt_type_name() {
        return nt_type_name;
    }

    public void setNt_type_name(String nt_type_name) {
        this.nt_type_name = nt_type_name;
    }
}
