package in.teamconsultants.dmac.model;

public class AccountDataObj {

    private String AId;
    private String BusinessLegalName;
    private String BusinessShortName;
    private String BusinessAddress;
    private String FolderName;
    private String CustomerAdminId;
    private String PinCode;
    private String CityCode;
    private String CustomerCode;
    private String IncomeTaxService;
    private String GSTService;
    private String RegistrationType;
    private String FEId;
    private String GSTRNo;
    private String GSTRegistrationFilePath;
    private String GSTPortalUserID;
    private String GSTPortalPassword;
    private String PANNo;
    private String PANCardFilePath;
    private String TANNo;
    private String ITRPassword;
    private String TypeOfEntity;
    private String PartnershipDeedFilePath;
    private String RegistrationCertificateFilePath;
    private String MOAFilePath;
    private String AOAFilePath;
    private String IncorporationCertificateFilePath;
    private String NameOfCAITP;
    private String AddressOfCAITP;
    private String PhoneOfCAITP;
    private String EmailOfCAITP;
    private String PreviousYearAuditSheetFilePath;
    private String StatusId;
    private String CreatedBy;
    private String CreatedAt;
    private String UpdatedBy;
    private String UpdatedAt;
    private String OthersDocFilePath;
    private String ShortName;
    private String FullName;


    public AccountDataObj() {
    }

    public AccountDataObj(String AId, String businessLegalName, String businessShortName, String businessAddress,
                          String folderName, String customerAdminId, String pinCode, String cityCode, String customerCode,
                          String incomeTaxService, String GSTService, String registrationType, String FEId, String GSTRNo,
                          String GSTRegistrationFilePath, String GSTPortalUserID, String GSTPortalPassword, String PANNo,
                          String PANCardFilePath, String TANNo, String ITRPassword, String typeOfEntity, String partnershipDeedFilePath,
                          String registrationCertificateFilePath, String MOAFilePath, String AOAFilePath,
                          String incorporationCertificateFilePath, String nameOfCAITP, String addressOfCAITP, String phoneOfCAITP,
                          String emailOfCAITP, String previousYearAuditSheetFilePath, String statusId, String createdBy,
                          String createdAt, String updatedBy, String updatedAt, String othersDocFilePath, String shortName,
                          String fullName) {
        this.AId = AId;
        BusinessLegalName = businessLegalName;
        BusinessShortName = businessShortName;
        BusinessAddress = businessAddress;
        FolderName = folderName;
        CustomerAdminId = customerAdminId;
        PinCode = pinCode;
        CityCode = cityCode;
        CustomerCode = customerCode;
        IncomeTaxService = incomeTaxService;
        this.GSTService = GSTService;
        RegistrationType = registrationType;
        this.FEId = FEId;
        this.GSTRNo = GSTRNo;
        this.GSTRegistrationFilePath = GSTRegistrationFilePath;
        this.GSTPortalUserID = GSTPortalUserID;
        this.GSTPortalPassword = GSTPortalPassword;
        this.PANNo = PANNo;
        this.PANCardFilePath = PANCardFilePath;
        this.TANNo = TANNo;
        this.ITRPassword = ITRPassword;
        TypeOfEntity = typeOfEntity;
        PartnershipDeedFilePath = partnershipDeedFilePath;
        RegistrationCertificateFilePath = registrationCertificateFilePath;
        this.MOAFilePath = MOAFilePath;
        this.AOAFilePath = AOAFilePath;
        IncorporationCertificateFilePath = incorporationCertificateFilePath;
        NameOfCAITP = nameOfCAITP;
        AddressOfCAITP = addressOfCAITP;
        PhoneOfCAITP = phoneOfCAITP;
        EmailOfCAITP = emailOfCAITP;
        PreviousYearAuditSheetFilePath = previousYearAuditSheetFilePath;
        StatusId = statusId;
        CreatedBy = createdBy;
        CreatedAt = createdAt;
        UpdatedBy = updatedBy;
        UpdatedAt = updatedAt;
        OthersDocFilePath = othersDocFilePath;
        ShortName = shortName;
        FullName = fullName;
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

    public String getBusinessAddress() {
        return BusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        BusinessAddress = businessAddress;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public String getCustomerAdminId() {
        return CustomerAdminId;
    }

    public void setCustomerAdminId(String customerAdminId) {
        CustomerAdminId = customerAdminId;
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

    public String getCustomerCode() {
        return CustomerCode;
    }

    public void setCustomerCode(String customerCode) {
        CustomerCode = customerCode;
    }

    public String getIncomeTaxService() {
        return IncomeTaxService;
    }

    public void setIncomeTaxService(String incomeTaxService) {
        IncomeTaxService = incomeTaxService;
    }

    public String getGSTService() {
        return GSTService;
    }

    public void setGSTService(String GSTService) {
        this.GSTService = GSTService;
    }

    public String getRegistrationType() {
        return RegistrationType;
    }

    public void setRegistrationType(String registrationType) {
        RegistrationType = registrationType;
    }

    public String getFEId() {
        return FEId;
    }

    public void setFEId(String FEId) {
        this.FEId = FEId;
    }

    public String getGSTRNo() {
        return GSTRNo;
    }

    public void setGSTRNo(String GSTRNo) {
        this.GSTRNo = GSTRNo;
    }

    public String getGSTRegistrationFilePath() {
        return GSTRegistrationFilePath;
    }

    public void setGSTRegistrationFilePath(String GSTRegistrationFilePath) {
        this.GSTRegistrationFilePath = GSTRegistrationFilePath;
    }

    public String getGSTPortalUserID() {
        return GSTPortalUserID;
    }

    public void setGSTPortalUserID(String GSTPortalUserID) {
        this.GSTPortalUserID = GSTPortalUserID;
    }

    public String getGSTPortalPassword() {
        return GSTPortalPassword;
    }

    public void setGSTPortalPassword(String GSTPortalPassword) {
        this.GSTPortalPassword = GSTPortalPassword;
    }

    public String getPANNo() {
        return PANNo;
    }

    public void setPANNo(String PANNo) {
        this.PANNo = PANNo;
    }

    public String getPANCardFilePath() {
        return PANCardFilePath;
    }

    public void setPANCardFilePath(String PANCardFilePath) {
        this.PANCardFilePath = PANCardFilePath;
    }

    public String getTANNo() {
        return TANNo;
    }

    public void setTANNo(String TANNo) {
        this.TANNo = TANNo;
    }

    public String getITRPassword() {
        return ITRPassword;
    }

    public void setITRPassword(String ITRPassword) {
        this.ITRPassword = ITRPassword;
    }

    public String getTypeOfEntity() {
        return TypeOfEntity;
    }

    public void setTypeOfEntity(String typeOfEntity) {
        TypeOfEntity = typeOfEntity;
    }

    public String getPartnershipDeedFilePath() {
        return PartnershipDeedFilePath;
    }

    public void setPartnershipDeedFilePath(String partnershipDeedFilePath) {
        PartnershipDeedFilePath = partnershipDeedFilePath;
    }

    public String getRegistrationCertificateFilePath() {
        return RegistrationCertificateFilePath;
    }

    public void setRegistrationCertificateFilePath(String registrationCertificateFilePath) {
        RegistrationCertificateFilePath = registrationCertificateFilePath;
    }

    public String getMOAFilePath() {
        return MOAFilePath;
    }

    public void setMOAFilePath(String MOAFilePath) {
        this.MOAFilePath = MOAFilePath;
    }

    public String getAOAFilePath() {
        return AOAFilePath;
    }

    public void setAOAFilePath(String AOAFilePath) {
        this.AOAFilePath = AOAFilePath;
    }

    public String getIncorporationCertificateFilePath() {
        return IncorporationCertificateFilePath;
    }

    public void setIncorporationCertificateFilePath(String incorporationCertificateFilePath) {
        IncorporationCertificateFilePath = incorporationCertificateFilePath;
    }

    public String getNameOfCAITP() {
        return NameOfCAITP;
    }

    public void setNameOfCAITP(String nameOfCAITP) {
        NameOfCAITP = nameOfCAITP;
    }

    public String getAddressOfCAITP() {
        return AddressOfCAITP;
    }

    public void setAddressOfCAITP(String addressOfCAITP) {
        AddressOfCAITP = addressOfCAITP;
    }

    public String getPhoneOfCAITP() {
        return PhoneOfCAITP;
    }

    public void setPhoneOfCAITP(String phoneOfCAITP) {
        PhoneOfCAITP = phoneOfCAITP;
    }

    public String getEmailOfCAITP() {
        return EmailOfCAITP;
    }

    public void setEmailOfCAITP(String emailOfCAITP) {
        EmailOfCAITP = emailOfCAITP;
    }

    public String getPreviousYearAuditSheetFilePath() {
        return PreviousYearAuditSheetFilePath;
    }

    public void setPreviousYearAuditSheetFilePath(String previousYearAuditSheetFilePath) {
        PreviousYearAuditSheetFilePath = previousYearAuditSheetFilePath;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
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

    public String getOthersDocFilePath() {
        return OthersDocFilePath;
    }

    public void setOthersDocFilePath(String othersDocFilePath) {
        OthersDocFilePath = othersDocFilePath;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
