package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.FileCategoryCount;

public class FileCategoryWiseCountResponse extends BaseResponse {

    private List<FileCategoryCount> FileCategoryCount;

    public FileCategoryWiseCountResponse() {
    }

    public FileCategoryWiseCountResponse(List<FileCategoryCount> fileCategoryCount) {
        FileCategoryCount = fileCategoryCount;
    }

    public List<FileCategoryCount> getFileCategoryCount() {
        return FileCategoryCount;
    }

    public void setFileCategoryCount(List<FileCategoryCount> fileCategoryCount) {
        FileCategoryCount = fileCategoryCount;
    }
}
