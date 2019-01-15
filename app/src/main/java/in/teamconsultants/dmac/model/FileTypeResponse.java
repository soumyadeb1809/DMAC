package in.teamconsultants.dmac.model;

import java.util.ArrayList;

public class FileTypeResponse {

    String Status;
    String Message;
    ArrayList<FileTypeObj> FileTypeList;

    public FileTypeResponse() {
    }

    public FileTypeResponse(String status, String message, ArrayList<FileTypeObj> fileTypeList) {
        Status = status;
        Message = message;
        FileTypeList = fileTypeList;
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

    public ArrayList<FileTypeObj> getFileTypeList() {
        return FileTypeList;
    }

    public void setFileTypeList(ArrayList<FileTypeObj> fileTypeList) {
        FileTypeList = fileTypeList;
    }

}
