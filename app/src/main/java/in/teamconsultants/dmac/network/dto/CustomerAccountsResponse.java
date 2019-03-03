package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.CustomerAccount;

public class CustomerAccountsResponse extends BaseResponse {

    private List<CustomerAccount> AccountList;

    public List<CustomerAccount> getAccountList() {
        return AccountList;
    }

    public void setAccountList(List<CustomerAccount> accountList) {
        AccountList = accountList;
    }

}
