package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.CustomerJob;

public class FileSearchResponse extends BaseResponse {

    private List<CustomerJob> SearchResultList;


    public List<CustomerJob> getSearchResultList() {
        return SearchResultList;
    }

    public void setSearchResultList(List<CustomerJob> searchResultList) {
        SearchResultList = searchResultList;
    }

}
