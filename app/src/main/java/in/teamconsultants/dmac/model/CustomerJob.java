package in.teamconsultants.dmac.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerJob implements Serializable {

    private String FId;
    private String Name;
    private String FilePath;
    private String Notes;
    private String StatusId;
    private String FileTypeId;
    private String AccountId;
    private String EndCustomerUserId;
    private String CreatedAt;
    private String UpdatedAt;
    private String FileName;

    public CustomerJob() {
    }

    public CustomerJob(String FId, String name, String filePath, String notes, String statusId, String fileTypeId,
                       String accountId, String endCustomerUserId, String createdAt, String updatedAt, String fileName) {
        this.FId = FId;
        Name = name;
        FilePath = filePath;
        Notes = notes;
        StatusId = statusId;
        FileTypeId = fileTypeId;
        AccountId = accountId;
        EndCustomerUserId = endCustomerUserId;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
        FileName = fileName;
    }

    public String getFId() {
        return FId;
    }

    public void setFId(String FId) {
        this.FId = FId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getFileTypeId() {
        return FileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        FileTypeId = fileTypeId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getEndCustomerUserId() {
        return EndCustomerUserId;
    }

    public void setEndCustomerUserId(String endCustomerUserId) {
        EndCustomerUserId = endCustomerUserId;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
