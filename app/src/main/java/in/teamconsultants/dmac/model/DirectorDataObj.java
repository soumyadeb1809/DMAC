package in.teamconsultants.dmac.model;

public class DirectorDataObj {

    private String DId;
    private String FullName;
    private String Email;
    private String Phone;
    private String PAN;
    private String PANFilePath;
    private String Aadhar;
    private String AadharFilePath;
    private String AccountId;
    private String CreatedAt;

    public DirectorDataObj() {
    }

    public DirectorDataObj(String DId, String fullName, String email, String phone, String PAN, String PANFilePath,
                           String aadhar, String aadharFilePath, String accountId, String createdAt) {
        this.DId = DId;
        FullName = fullName;
        Email = email;
        Phone = phone;
        this.PAN = PAN;
        this.PANFilePath = PANFilePath;
        Aadhar = aadhar;
        AadharFilePath = aadharFilePath;
        AccountId = accountId;
        CreatedAt = createdAt;
    }

    public String getDId() {
        return DId;
    }

    public void setDId(String DId) {
        this.DId = DId;
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

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getPANFilePath() {
        return PANFilePath;
    }

    public void setPANFilePath(String PANFilePath) {
        this.PANFilePath = PANFilePath;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String aadhar) {
        Aadhar = aadhar;
    }

    public String getAadharFilePath() {
        return AadharFilePath;
    }

    public void setAadharFilePath(String aadharFilePath) {
        AadharFilePath = aadharFilePath;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
