package in.teamconsultants.dmac.ui.home.invoices;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Invoice;
import in.teamconsultants.dmac.utils.AppConstants;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private List<Invoice> invoiceList;
    private Activity activity;

    private Invoice selectedInvoice;
    private InvoiceViewHolder selectedInvoiceHolder;

    private OnInvoiceSelectedListener invoiceSelectedListener;

    public class InvoiceViewHolder extends RecyclerView.ViewHolder{

        public TextView tvInvoiceName, tvNotes, tvAmount, tvDate, tvStatus;
        public View extraSpace;
        public LinearLayout grpContent;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInvoiceName = itemView.findViewById(R.id.txt_invoice_name);
            tvNotes = itemView.findViewById(R.id.txt_notes);
            tvDate = itemView.findViewById(R.id.txt_date);
            tvStatus = itemView.findViewById(R.id.txt_status);
            tvAmount = itemView.findViewById(R.id.txt_amount);

            grpContent = itemView.findViewById(R.id.grp_content);
            extraSpace = itemView.findViewById(R.id.extra_space);

        }
    }

    public InvoiceAdapter(Activity activity, List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
        this.activity = activity;
        if(activity instanceof OnInvoiceSelectedListener){
            invoiceSelectedListener = (OnInvoiceSelectedListener) activity;
        }
        else {
            throw new RuntimeException("Activity must implement OnInvoiceSelectedListener");
        }

    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_invoice, viewGroup,false);
        return new InvoiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final InvoiceViewHolder viewHolder, final int position) {

        final Invoice invoice = invoiceList.get(position);

        if(invoice != null){

            viewHolder.tvInvoiceName.setText(invoice.getInvoiceName());
            viewHolder.tvAmount.setText("₹" + invoice.getInvoiceAmount());
            viewHolder.tvNotes.setText(invoice.getNotes());

            String createDate = invoice.getCreatedAt();
            String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateTimeFormat);
            String finalDate = createDate;
            try{
                Date date = simpleDateFormat.parse(createDate);
                String datePattern = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                finalDate = dateFormat.format(date);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            viewHolder.tvDate.setText(finalDate);

            viewHolder.tvStatus.setText(invoice.getStatus());

            if(invoice.getStatus().equals("Paid")){
               viewHolder.tvStatus.setBackgroundResource(R.drawable.back_green_rounded);
            }
            else {
                viewHolder.tvStatus.setBackgroundResource(R.drawable.back_light_blue_rounded);
            }

            if(selectedInvoiceHolder != null && viewHolder == selectedInvoiceHolder){
                viewHolder.grpContent.setBackgroundResource(R.drawable.back_invoice_selected_yellow);
            }
            else {
                viewHolder.grpContent.setBackgroundResource(R.drawable.back_white_accent_rounded);
            }

            viewHolder.grpContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedInvoice == invoice) {

                        selectedInvoice = null;
                        viewHolder.grpContent.setBackgroundResource(R.drawable.back_white_accent_rounded);
                        invoiceSelectedListener.updateSelectedInvoiceItem(selectedInvoice);
                        invoiceSelectedListener.updatePayButtonVisibility(false);

                    } else {
                        selectedInvoice = invoice;
                        invoiceSelectedListener.updateSelectedInvoiceItem(selectedInvoice);
                        if(null != selectedInvoiceHolder) {
                            selectedInvoiceHolder.grpContent.setBackgroundResource(R.drawable.back_white_accent_rounded);
                        }
                        viewHolder.grpContent.setBackgroundResource(R.drawable.back_invoice_selected_yellow);
                        selectedInvoiceHolder = viewHolder;
                        invoiceSelectedListener.updatePayButtonVisibility(true);
                    }
                }
            });

            if(position == invoiceList.size() - 1){
                viewHolder.extraSpace.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.extraSpace.setVisibility(View.GONE);
            }

        }
        else {
            Log.d(AppConstants.LOG_TAG, "Invoice = null at position: " + position);
        }


    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    /**
     * Interface to communicate with the activity to update the selected invoice
     * that is to be used to proceed with the payment.
     */

    public interface OnInvoiceSelectedListener {

        void updateSelectedInvoiceItem(Invoice invoice);

        void updatePayButtonVisibility(boolean showPayBtn);

    }


}
