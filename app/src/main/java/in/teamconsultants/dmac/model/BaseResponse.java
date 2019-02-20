package in.teamconsultants.dmac.model;

public class BaseResponse {

    private String Status;
    private String Message;

    public BaseResponse() {
    }

    public BaseResponse(String status, String message) {
        Status = status;
        Message = message;
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
}
