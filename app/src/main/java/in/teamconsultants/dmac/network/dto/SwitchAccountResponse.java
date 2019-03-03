package in.teamconsultants.dmac.network.dto;

import in.teamconsultants.dmac.model.UserData;

public class SwitchAccountResponse extends BaseResponse {

    private UserData UserData;
    private String TokenId;

    public UserData getUserData() {
        return UserData;
    }

    public void setUserData(UserData userData) {
        UserData = userData;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }
}
