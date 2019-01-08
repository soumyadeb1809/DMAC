package in.teamconsultants.dmac.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerJob implements Serializable {

    private String jobName;
    private String createDate;
    private String updateDate;
    private String jobStatus;
    private String accountName;
    private String endCustomer;
    private String jobUrl;
    private ArrayList<JobUploadFile> jobUploadFiles;

    public CustomerJob(){
        // Empty constructor
    }

    public CustomerJob(String jobName, String createDate, String updateDate, String jobStatus,
                       String accountName, String endCustomer, String jobUrl, ArrayList<JobUploadFile> jobUploadFiles) {
        this.jobName = jobName;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.jobStatus = jobStatus;
        this.accountName = accountName;
        this.endCustomer = endCustomer;
        this.jobUploadFiles = jobUploadFiles;
        this.jobUrl = jobUrl;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getEndCustomer() {
        return endCustomer;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public ArrayList<JobUploadFile> getJobUploadFiles() {
        return jobUploadFiles;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setEndCustomer(String endCustomer) {
        this.endCustomer = endCustomer;
    }

    public void setJobUploadFiles(ArrayList<JobUploadFile> jobUploadFiles) {
        this.jobUploadFiles = jobUploadFiles;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }
}
