package in.teamconsultants.dmac.model;

public class NotificationType {

    private String nt_id;
    private String nt_type_name;

    public NotificationType() {
    }

    public NotificationType(String nt_id, String nt_type_name) {
        this.nt_id = nt_id;
        this.nt_type_name = nt_type_name;
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
