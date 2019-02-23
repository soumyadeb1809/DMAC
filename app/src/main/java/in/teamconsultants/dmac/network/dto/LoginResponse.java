package in.teamconsultants.dmac.network.dto;

import in.teamconsultants.dmac.model.UserData;

public class LoginResponse {
    private String Status;
    private UserData UserData;
    private String TokenId;
    private String Message;

    public LoginResponse() {
    }

    public LoginResponse(String status, in.teamconsultants.dmac.model.UserData userData, String tokenId, String message) {
        Status = status;
        UserData = userData;
        TokenId = tokenId;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public in.teamconsultants.dmac.model.UserData getUserData() {
        return UserData;
    }

    public void setUserData(in.teamconsultants.dmac.model.UserData userData) {
        UserData = userData;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
