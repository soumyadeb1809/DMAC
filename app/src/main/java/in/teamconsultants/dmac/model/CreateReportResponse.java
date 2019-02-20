package in.teamconsultants.dmac.model;

public class CreateReportResponse extends BaseResponse {

    private String ReportId;

    public CreateReportResponse() {
    }

    public CreateReportResponse(String reportId) {
        ReportId = reportId;
    }

    public String getReportId() {
        return ReportId;
    }

    public void setReportId(String reportId) {
        ReportId = reportId;
    }

}
