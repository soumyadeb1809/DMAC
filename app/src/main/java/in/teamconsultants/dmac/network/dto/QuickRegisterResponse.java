package in.teamconsultants.dmac.network.dto;

import in.teamconsultants.dmac.model.QuickRegisterResponseDetails;

public class QuickRegisterResponse {

    private String Status;
    private String AccountId;
    private String Message;
    private QuickRegisterResponseDetails Details;

    public QuickRegisterResponse() {
    }

    public QuickRegisterResponse(String status, String accountId, String message, QuickRegisterResponseDetails details) {
        Status = status;
        AccountId = accountId;
        Message = message;
        Details = details;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public QuickRegisterResponseDetails getDetails() {
        return Details;
    }

    public void setDetails(QuickRegisterResponseDetails details) {
        Details = details;
    }
}
