package in.teamconsultants.dmac.network.dto;

import java.util.ArrayList;

import in.teamconsultants.dmac.model.AccountSearchResultObj;

public class AccountSearchResponse extends BaseResponse {


    private ArrayList<AccountSearchResultObj> SearchResultList;

    public AccountSearchResponse() {
    }

    public AccountSearchResponse(String status, String message, ArrayList<AccountSearchResultObj> searchResultList) {
         SearchResultList = searchResultList;
    }


    public ArrayList<AccountSearchResultObj> getSearchResultList() {
        return SearchResultList;
    }

    public void setSearchResultList(ArrayList<AccountSearchResultObj> searchResultList) {
        SearchResultList = searchResultList;
    }
}
