package in.teamconsultants.dmac.model;

public class DirectorDocuments {
    private String imgAadharFilePath;
    private String imgPANFilePath;

    public DirectorDocuments() {
    }

    public DirectorDocuments(String imgAadharFilePath, String imgPANFilePath) {
        this.imgAadharFilePath = imgAadharFilePath;
        this.imgPANFilePath = imgPANFilePath;
    }

    public String getImgAadharFilePath() {
        return imgAadharFilePath;
    }

    public void setImgAadharFilePath(String imgAadharFilePath) {
        this.imgAadharFilePath = imgAadharFilePath;
    }

    public String getImgPANFilePath() {
        return imgPANFilePath;
    }

    public void setImgPANFilePath(String imgPANFilePath) {
        this.imgPANFilePath = imgPANFilePath;
    }
}
