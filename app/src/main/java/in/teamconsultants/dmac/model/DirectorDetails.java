package in.teamconsultants.dmac.model;

public class DirectorDetails {
    private String fullName;
    private String email;
    private String phone;
    private String aadhar;
    private String pan;
    private  DirectorDocuments directorDocuments;
    private boolean isAdmin;

    public DirectorDetails() {
    }

    public DirectorDetails(String fullName, String email, String phone, String aadhar, String pan,
                           DirectorDocuments directorDocuments, boolean isAdmin) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
        this.pan = pan;
        this.directorDocuments = directorDocuments;
        this.isAdmin = isAdmin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public DirectorDocuments getDirectorDocuments() {
        return directorDocuments;
    }

    public void setDirectorDocuments(DirectorDocuments directorDocuments) {
        this.directorDocuments = directorDocuments;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
