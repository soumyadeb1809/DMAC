package in.teamconsultants.dmac.model;

public class Account {

    private String legalName;
    private String shortName;
    private String customerCode;
    private String gstrNumber;
    private String panNumber;
    private String accountStatus;
    private String createdDate;
    private String updatedDate;
    private String createdBy;

    public Account(){
        // Empty constructor
    }

    public Account(String legalName, String shortName, String customerCode, String gstrNumber,
                   String panNumber, String accountStatus, String createdDate, String updatedDate, String createdBy) {
        this.legalName = legalName;
        this.shortName = shortName;
        this.customerCode = customerCode;
        this.gstrNumber = gstrNumber;
        this.panNumber = panNumber;
        this.accountStatus = accountStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdBy = createdBy;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getGstrNumber() {
        return gstrNumber;
    }

    public void setGstrNumber(String gstrNumber) {
        this.gstrNumber = gstrNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
