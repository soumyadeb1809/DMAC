package in.teamconsultants.dmac.model;

public class CreateAccountResponseDetails {

    private String Email;
    private String Phone;
    private String Password;

    public CreateAccountResponseDetails() {
    }

    public CreateAccountResponseDetails(String email, String phone, String password) {
        Email = email;
        Phone = phone;
        Password = password;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
