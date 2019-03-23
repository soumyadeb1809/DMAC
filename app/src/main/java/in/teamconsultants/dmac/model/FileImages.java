package in.teamconsultants.dmac.model;

import java.util.ArrayList;

public class FileImages {

    private ArrayList<String> imageUrls;

    void FileImages() {
        imageUrls = new ArrayList<>();
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
