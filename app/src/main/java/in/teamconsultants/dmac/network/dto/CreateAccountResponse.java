package in.teamconsultants.dmac.network.dto;

import in.teamconsultants.dmac.model.CreateAccountResponseDetails;

public class CreateAccountResponse {

    private String Status;
    private String AccountId;
    private String Message;
    private CreateAccountResponseDetails Details;

    public CreateAccountResponse() {
    }

    public CreateAccountResponse(String status, String accountId, String message, CreateAccountResponseDetails details) {
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

    public CreateAccountResponseDetails getDetails() {
        return Details;
    }

    public void setDetails(CreateAccountResponseDetails details) {
        Details = details;
    }
}
