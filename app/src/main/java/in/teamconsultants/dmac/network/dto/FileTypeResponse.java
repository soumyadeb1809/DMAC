package in.teamconsultants.dmac.network.dto;

import java.util.ArrayList;

import in.teamconsultants.dmac.model.FileTypeObj;

public class FileTypeResponse extends BaseResponse {

    ArrayList<FileTypeObj> FileTypeList;

    public ArrayList<FileTypeObj> getFileTypeList() {
        return FileTypeList;
    }

    public void setFileTypeList(ArrayList<FileTypeObj> fileTypeList) {
        FileTypeList = fileTypeList;
    }

}
