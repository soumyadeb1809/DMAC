package in.teamconsultants.dmac.model;

public class FileCategoryCount {

    private String TotalCount;
    private String FileCategory;

    public FileCategoryCount() {
    }

    public FileCategoryCount(String totalCount, String fileCategory) {
        TotalCount = totalCount;
        FileCategory = fileCategory;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getFileCategory() {
        return FileCategory;
    }

    public void setFileCategory(String fileCategory) {
        FileCategory = fileCategory;
    }
}
