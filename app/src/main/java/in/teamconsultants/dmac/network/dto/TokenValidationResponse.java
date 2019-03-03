package in.teamconsultants.dmac.network.dto;

public class TokenValidationResponse extends BaseResponse {

    private String UserId;
    private String AccountId;
    private String TokenId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

}
