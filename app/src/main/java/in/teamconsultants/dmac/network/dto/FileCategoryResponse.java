package in.teamconsultants.dmac.network.dto;

import java.util.ArrayList;

import in.teamconsultants.dmac.model.FileCategoryObj;

public class FileCategoryResponse {

    private String Status;
    private String Message;
    private ArrayList<FileCategoryObj> FileCategoryList;

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

    public ArrayList<FileCategoryObj> getFileCategoryList() {
        return FileCategoryList;
    }

    public void setFileCategoryList(ArrayList<FileCategoryObj> fileCategoryList) {
        FileCategoryList = fileCategoryList;
    }
}
