package in.teamconsultants.dmac.network.dto;

public class ProfileImageUploadResponse extends BaseResponse{
    private String ProfilePicPath;

    public String getProfilePicPath() {
        return ProfilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        ProfilePicPath = profilePicPath;
    }

}
