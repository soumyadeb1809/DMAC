package in.teamconsultants.dmac.ui.home.accounts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Account;
import in.teamconsultants.dmac.model.AccountSearchResultObj;

public class AccountListAdapter  extends RecyclerView.Adapter<AccountListAdapter.AccountsViewHolder> {

    private Context context;
    private ArrayList<AccountSearchResultObj> accountsList;
    private HashMap<String, String> statusMap;

    public AccountListAdapter(Context context, ArrayList<AccountSearchResultObj> accountsList, HashMap<String, String> statusMap) {
        this.context = context;
        this.accountsList = accountsList;
        this.statusMap = statusMap;
    }


    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLegalName, tvShortName, tvCustomerCode, tvCity, tvPanNumber;
        public TextView tvAccountStatus, tvCreateDate;
        public View view;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvLegalName = itemView.findViewById(R.id.txt_business_legal_name);
            tvShortName = itemView.findViewById(R.id.txt_business_short_name);
            tvCustomerCode = itemView.findViewById(R.id.txt_customer_code);
            tvCity = itemView.findViewById(R.id.txt_gstr_number);
            tvPanNumber = itemView.findViewById(R.id.txt_pan_number);
            tvCreateDate = itemView.findViewById(R.id.txt_created_on);
            tvAccountStatus = itemView.findViewById(R.id.txt_account_status);

        }
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_account, viewGroup,false);
        return new AccountListAdapter.AccountsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder accountsViewHolder, int position) {

        //Account account = accountsList.get(position);

        AccountSearchResultObj account = accountsList.get(position);

        accountsViewHolder.tvLegalName.setText(account.getBusinessLegalName());
        accountsViewHolder.tvShortName.setText(account.getBusinessShortName());
        if(TextUtils.isEmpty(account.getCustomerCode())){
            accountsViewHolder.tvCustomerCode.setText("NA");
        }
        else {
            accountsViewHolder.tvCustomerCode.setText(account.getCustomerCode());
        }
        if(TextUtils.isEmpty(account.getCityCode())){
            accountsViewHolder.tvCity.setText("NA");
        }
        else {
            accountsViewHolder.tvCity.setText(account.getCityCode());
        }
        if(TextUtils.isEmpty(account.getPANNo())){
            accountsViewHolder.tvPanNumber.setText(account.getPANNo());
        }
        else {
            accountsViewHolder.tvPanNumber.setText(account.getPANNo());
        }
        accountsViewHolder.tvAccountStatus.setText(statusMap.get(account.getStatusId()).trim());

        String createDate = account.getCreatedAt();
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

        accountsViewHolder.tvCreateDate.setText(finalDate);

        accountsViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AccountDetailActivity.class));
            }
        });

        if (position == accountsList.size() - 1){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(40, 40, 40, 200);
            accountsViewHolder.view.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        return accountsList.size();
    }


}
