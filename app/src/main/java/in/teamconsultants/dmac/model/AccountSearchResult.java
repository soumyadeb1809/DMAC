package in.teamconsultants.dmac.model;

import java.util.ArrayList;

public class AccountSearchResult {

    private String Status;
    private String Message;
    private ArrayList<AccountSearchResultObj> SearchResultList;

    public AccountSearchResult() {
    }

    public AccountSearchResult(String status, String message, ArrayList<AccountSearchResultObj> searchResultList) {
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

    public ArrayList<AccountSearchResultObj> getSearchResultList() {
        return SearchResultList;
    }

    public void setSearchResultList(ArrayList<AccountSearchResultObj> searchResultList) {
        SearchResultList = searchResultList;
    }
}
