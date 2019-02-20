package in.teamconsultants.dmac.model;

import java.util.List;

public class ReportTypeResponse extends BaseResponse {

    private List<ReportType> report_type_list;

    public ReportTypeResponse() {
    }

    public ReportTypeResponse(List<ReportType> report_type_list) {
        this.report_type_list = report_type_list;
    }

    public List<ReportType> getReport_type_list() {
        return report_type_list;
    }

    public void setReport_type_list(List<ReportType> report_type_list) {
        this.report_type_list = report_type_list;
    }

}
