package in.teamconsultants.dmac.model;

public class CreateJobResponse {
    String Status;
    String JobId;
    String Message;

    public CreateJobResponse() {
    }

    public CreateJobResponse(String status, String jobId, String message) {
        Status = status;
        JobId = jobId;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
