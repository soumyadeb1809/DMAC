package in.teamconsultants.dmac.model;

public class AccountSearchResultObj {

    private String AId;
    private String BusinessLegalName;
    private String BusinessShortName;
    private String CustomerCode;
    private String CreatedAt;
    private String StatusId;
    private String PinCode;
    private String CityCode;
    private String PANNo;

    public AccountSearchResultObj() {
    }

    public AccountSearchResultObj(String AId, String businessLegalName, String businessShortName,
                                  String customerCode, String createdAt, String statusId, String pinCode, String cityCode, String PANNo) {
        this.AId = AId;
        BusinessLegalName = businessLegalName;
        BusinessShortName = businessShortName;
        CustomerCode = customerCode;
        CreatedAt = createdAt;
        StatusId = statusId;
        PinCode = pinCode;
        CityCode = cityCode;
        this.PANNo = PANNo;
    }

    public String getAId() {
        return AId;
    }

    public void setAId(String AId) {
        this.AId = AId;
    }

    public String getBusinessLegalName() {
        return BusinessLegalName;
    }

    public void setBusinessLegalName(String businessLegalName) {
        BusinessLegalName = businessLegalName;
    }

    public String getBusinessShortName() {
        return BusinessShortName;
    }

    public void setBusinessShortName(String businessShortName) {
        BusinessShortName = businessShortName;
    }

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getPANNo() {
        return PANNo;
    }

    public void setPANNo(String PANNo) {
        this.PANNo = PANNo;
    }
}
