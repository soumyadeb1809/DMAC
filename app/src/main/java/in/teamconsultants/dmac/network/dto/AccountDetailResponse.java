package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.AccountDataObj;
import in.teamconsultants.dmac.model.DirectorDataObj;
import in.teamconsultants.dmac.model.KeyUserDataObj;

public class AccountDetailResponse {

    private String Status;
    private String Message;
    private AccountDataObj AccountData;
    private List<DirectorDataObj> DirectorData;
    private KeyUserDataObj KeyUserData;

    public AccountDetailResponse() {
    }

    public AccountDetailResponse(String status, String message, AccountDataObj accountData,
                                 List<DirectorDataObj> directorData, KeyUserDataObj keyUserData) {
        Status = status;
        Message = message;
        AccountData = accountData;
        DirectorData = directorData;
        KeyUserData = keyUserData;
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
