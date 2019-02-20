package in.teamconsultants.dmac.model;

public class ReportType {

    private String rt_id;
    private String rt_name;

    public ReportType() {
    }

    public ReportType(String rt_id, String rt_name) {
        this.rt_id = rt_id;
        this.rt_name = rt_name;
    }

    public String getRt_id() {
        return rt_id;
    }

    public void setRt_id(String rt_id) {
        this.rt_id = rt_id;
    }

    public String getRt_name() {
        return rt_name;
    }

    public void setRt_name(String rt_name) {
        this.rt_name = rt_name;
    }

}
