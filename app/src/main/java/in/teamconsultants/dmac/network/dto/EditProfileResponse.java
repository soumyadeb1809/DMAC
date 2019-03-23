package in.teamconsultants.dmac.network.dto;

import in.teamconsultants.dmac.model.UserData;

public class EditProfileResponse extends BaseResponse {

    private UserData UserData;

    public UserData getUserData() {
        return UserData;
    }

    public void setUserData(UserData userData) {
        UserData = userData;
    }
}
