package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.AccountDataObj;
import in.teamconsultants.dmac.model.DirectorDataObj;
import in.teamconsultants.dmac.model.KeyUserDataObj;

public class AccountDetailResponse extends BaseResponse {
    private AccountDataObj AccountData;
    private List<DirectorDataObj> DirectorData;
    private KeyUserDataObj KeyUserData;

    public AccountDataObj getAccountData() {
        return AccountData;
    }

    public void setAccountData(AccountDataObj accountData) {
        AccountData = accountData;
    }

    public List<DirectorDataObj> getDirectorData() {
        return DirectorData;
    }

    public void setDirectorData(List<DirectorDataObj> directorData) {
        DirectorData = directorData;
    }

    public KeyUserDataObj getKeyUserData() {
        return KeyUserData;
    }

    public void setKeyUserData(KeyUserDataObj keyUserData) {
        KeyUserData = keyUserData;
    }
}
