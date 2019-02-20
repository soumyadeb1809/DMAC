package in.teamconsultants.dmac.model;

public class Report {

    private String report_id;
    private String report_type_id;
    private String report_from;
    private String report_to;
    private String report_path;
    private String status_id;
    private String account_id;
    private String requested_by;
    private String created_at;
    private String uploaded_by;
    private String uploaded_at;
    private String rt_name;
    private String Status;
    private String BusinessShortName;
    private String RequestedBy;
    private String UploadedBy;

    public Report() {
    }

    public Report(String report_id, String report_type_id, String report_from, String report_to,
                  String report_path, String status_id, String account_id, String requested_by,
                  String created_at, String uploaded_by, String uploaded_at, String rt_name,
                  String status, String businessShortName, String requestedBy, String uploadedBy) {
        this.report_id = report_id;
        this.report_type_id = report_type_id;
        this.report_from = report_from;
        this.report_to = report_to;
        this.report_path = report_path;
        this.status_id = status_id;
        this.account_id = account_id;
        this.requested_by = requested_by;
        this.created_at = created_at;
        this.uploaded_by = uploaded_by;
        this.uploaded_at = uploaded_at;
        this.rt_name = rt_name;
        Status = status;
        BusinessShortName = businessShortName;
        RequestedBy = requestedBy;
        UploadedBy = uploadedBy;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getReport_type_id() {
        return report_type_id;
    }

    public void setReport_type_id(String report_type_id) {
        this.report_type_id = report_type_id;
    }

    public String getReport_from() {
        return report_from;
    }

    public void setReport_from(String report_from) {
        this.report_from = report_from;
    }

    public String getReport_to() {
        return report_to;
    }

    public void setReport_to(String report_to) {
        this.report_to = report_to;
    }

    public String getReport_path() {
        return report_path;
    }

    public void setReport_path(String report_path) {
        this.report_path = report_path;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(String uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public String getRt_name() {
        return rt_name;
    }

    public void setRt_name(String rt_name) {
        this.rt_name = rt_name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBusinessShortName() {
        return BusinessShortName;
    }

    public void setBusinessShortName(String businessShortName) {
        BusinessShortName = businessShortName;
    }

    public String getRequestedBy() {
        return RequestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        RequestedBy = requestedBy;
    }

    public String getUploadedBy() {
        return UploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        UploadedBy = uploadedBy;
    }
}
