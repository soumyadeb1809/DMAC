package in.teamconsultants.dmac.model;

public class FileCountResponse {

    private String StatusId;
    private String AccountId;
    private int FileCount;


    public FileCountResponse() {
    }

    public FileCountResponse(String statusId, String accountId, int fileCount) {
        StatusId = statusId;
        AccountId = accountId;
        FileCount = fileCount;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public int getFileCount() {
        return FileCount;
    }

    public void setFileCount(int fileCount) {
        FileCount = fileCount;
    }
}
