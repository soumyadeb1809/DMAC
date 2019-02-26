package in.teamconsultants.dmac.network.dto;

import java.util.List;

import in.teamconsultants.dmac.model.Invoice;

public class InvoiceResponse extends BaseResponse {

    private List<Invoice> InvoiceList;

    public List<Invoice> getInvoiceList() {
        return InvoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        InvoiceList = invoiceList;
    }

}
