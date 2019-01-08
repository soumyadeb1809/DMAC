package in.teamconsultants.dmac.model;

import java.io.Serializable;

public class JobUploadFile implements Serializable {

    private int fileNo;
    private String fileCategory;
    private String fileType;
    private String fileStatus;
    private String fileUrl;
    private boolean uploadAgain;
    private String fileNotes;

    public JobUploadFile() {
        // Empty constructor
    }

    public JobUploadFile(int fileNo, String fileCategory, String fileType, String fileStatus, String fileUrl,
                         boolean uploadAgain, String fileNotes) {
        this.fileNo = fileNo;
        this.fileCategory = fileCategory;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.fileUrl = fileUrl;
        this.uploadAgain = uploadAgain;
        this.fileNotes = fileNotes;
    }

    public int getFileNo() {
        return fileNo;
    }

    public void setFileNo(int fileNo) {
        this.fileNo = fileNo;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isUploadAgain() {
        return uploadAgain;
    }

    public void setUploadAgain(boolean uploadAgain) {
        this.uploadAgain = uploadAgain;
    }

    public String getFileNotes() {
        return fileNotes;
    }

    public void setFileNotes(String fileNotes) {
        this.fileNotes = fileNotes;
    }

}
