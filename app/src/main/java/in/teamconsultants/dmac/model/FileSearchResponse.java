package in.teamconsultants.dmac.model;

import java.util.List;

public class FileSearchResponse {

    private String Status;
    private String Message;
    private List<CustomerJob> SearchResultList;

    public FileSearchResponse() {
    }

    public FileSearchResponse(String status, String message, List<CustomerJob> searchResultList) {
        Status = status;
        Message = message;
        SearchResultList = searchResultList;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<CustomerJob> getSearchResultList() {
        return SearchResultList;
    }

    public void setSearchResultList(List<CustomerJob> searchResultList) {
        SearchResultList = searchResultList;
    }

}
