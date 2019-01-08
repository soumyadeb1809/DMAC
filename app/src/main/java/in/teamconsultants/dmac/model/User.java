package in.teamconsultants.dmac.model;

public class User {

    private String name;
    private String email;
    private String phone;
    private String role;
    private String accountName;
    private boolean keyUser;
    private String createdAt;
    private String updatedAt;

    public User() {
        // Empty constructor
    }

    public User(String name, String email, String phone, String role, String accountName,
                boolean keyUser, String createdAt, String updatedAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.accountName = accountName;
        this.keyUser = keyUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isKeyUser() {
        return keyUser;
    }

    public void setKeyUser(boolean keyUser) {
        this.keyUser = keyUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
