package in.teamconsultants.dmac.model;

import java.util.List;

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

    public String getMessage() {
        return Message;
    }

    public AccountDataObj getAccountDataObj() {
        return AccountData;
    }

    public List<DirectorDataObj> getDirectorDataObj() {
        return DirectorData;
    }

    public KeyUserDataObj getKeyUserDataObj() {
        return KeyUserData;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setAccountData(AccountDataObj accountData) {
        AccountData = accountData;
    }

    public void setDirectorData(List<DirectorDataObj> directorData) {
        DirectorData = directorData;
    }

    public void setKeyUserData(KeyUserDataObj keyUserData) {
        KeyUserData = keyUserData;
    }
}
