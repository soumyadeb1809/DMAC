package in.teamconsultants.dmac.model;

public class Invoice {

    private String IId;
    private String AccountId;
    private String InvoiceAmount;
    private String StateGST;
    private String CentralGST;
    private String TotalGST;
    private String StatusId;
    private String TotalAmount;
    private String DueDate;
    private String BillTo;
    private String Notes;
    private String CreatedAt;
    private String CreatedBy;
    private String InvoiceFilePath;
    private String UpdatedAt;
    private String UpdatedBy;
    private String InvoiceName;
    private String BusinessLegalName;
    private String BusinessShortName;
    private String PANNo;
    private String FullName;
    private String ShortName;

    public String getIId() {
        return IId;
    }

    public void setIId(String IId) {
        this.IId = IId;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getInvoiceAmount() {
        return InvoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        InvoiceAmount = invoiceAmount;
    }

    public String getStateGST() {
        return StateGST;
    }

    public void setStateGST(String stateGST) {
        StateGST = stateGST;
    }

    public String getCentralGST() {
        return CentralGST;
    }

    public void setCentralGST(String centralGST) {
        CentralGST = centralGST;
    }

    public String getTotalGST() {
        return TotalGST;
    }

    public void setTotalGST(String totalGST) {
        TotalGST = totalGST;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getBillTo() {
        return BillTo;
    }

    public void setBillTo(String billTo) {
        BillTo = billTo;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getInvoiceFilePath() {
        return InvoiceFilePath;
    }

    public void setInvoiceFilePath(String invoiceFilePath) {
        InvoiceFilePath = invoiceFilePath;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getInvoiceName() {
        return InvoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        InvoiceName = invoiceName;
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

    public String getPANNo() {
        return PANNo;
    }

    public void setPANNo(String PANNo) {
        this.PANNo = PANNo;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }
}
