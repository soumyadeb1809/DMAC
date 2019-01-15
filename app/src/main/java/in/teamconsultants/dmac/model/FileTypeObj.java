package in.teamconsultants.dmac.model;

public class FileTypeObj {

    String FTId;
    String FileName;
    String FileCategory;

    public FileTypeObj() {
    }

    public FileTypeObj(String FTId, String fileName, String fileCategory) {
        this.FTId = FTId;
        FileName = fileName;
        FileCategory = fileCategory;
    }

    public String getFTId() {
        return FTId;
    }

    public void setFTId(String FTId) {
        this.FTId = FTId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileCategory() {
        return FileCategory;
    }

    public void setFileCategory(String fileCategory) {
        FileCategory = fileCategory;
    }
}
