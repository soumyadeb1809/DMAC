package in.teamconsultants.dmac.network.dto;

public class AccountCountResponse extends BaseResponse {
    private String StatusId;
    private String UserId;
    private int AccountCount;

    public AccountCountResponse() {
    }

    public AccountCountResponse(String statusId, String userId, int accountCount) {
        StatusId = statusId;
        UserId = userId;
        AccountCount = accountCount;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getAccountCount() {
        return AccountCount;
    }

    public void setAccountCount(int accountCount) {
        AccountCount = accountCount;
    }
}
