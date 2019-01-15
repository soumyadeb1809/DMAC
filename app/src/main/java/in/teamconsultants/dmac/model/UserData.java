package in.teamconsultants.dmac.model;

public class UserData {

    private String UId;
    private String FullName;
    private String Email;
    private String Phone;
    private String KeyUser;
    private String AccountId;
    private String StatusId;
    private String RoleId;
    private String CreatedBy;
    private String CreatedAt;
    private String UpdatedBy;
    private String UpdatedAt;
    private String EmailVerified;

    public UserData() {
    }

    public UserData(String UId, String fullName, String email, String phone, String keyUser,
                    String accountId, String statusId, String roleId, String createdBy, String createdAt, String updatedBy, String updatedAt, String emailVerified) {
        this.UId = UId;
        FullName = fullName;
        Email = email;
        Phone = phone;
        KeyUser = keyUser;
        AccountId = accountId;
        StatusId = statusId;
        RoleId = roleId;
        CreatedBy = createdBy;
        CreatedAt = createdAt;
        UpdatedBy = updatedBy;
        UpdatedAt = updatedAt;
        EmailVerified = emailVerified;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getKeyUser() {
        return KeyUser;
    }

    public void setKeyUser(String keyUser) {
        KeyUser = keyUser;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getEmailVerified() {
        return EmailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        EmailVerified = emailVerified;
    }
}
