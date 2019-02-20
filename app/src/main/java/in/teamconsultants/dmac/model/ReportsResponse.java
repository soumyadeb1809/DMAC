package in.teamconsultants.dmac.model;

import java.util.List;

public class ReportsResponse extends  BaseResponse {

    private List<Report> SearchResultList;

    public ReportsResponse() {
    }

    public ReportsResponse(List<Report> searchResultList) {
        SearchResultList = searchResultList;
    }

    public List<Report> getSearchResultList() {
        return SearchResultList;
    }

    public void setSearchResultList(List<Report> searchResultList) {
        SearchResultList = searchResultList;
    }

}
