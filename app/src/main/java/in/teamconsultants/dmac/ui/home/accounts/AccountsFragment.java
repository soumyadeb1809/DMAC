package in.teamconsultants.dmac.ui.home.accounts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Account;


public class AccountsFragment extends Fragment {

    private OnAccountsInteractionListener mListener;

    private ArrayList<Account> accountsList;
    private RecyclerView rvAccountsList;

    private AccountListAdapter accountListAdapter;

    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accounts, container, false);

        initializeUi(v);

        populateUi();

        return v;
    }

    private void initializeUi(View v) {

        rvAccountsList = v.findViewById(R.id.rv_accounts_list);

        accountsList = new ArrayList<>();
        accountListAdapter = new AccountListAdapter(getContext(), accountsList);

        rvAccountsList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAccountsList.setAdapter(accountListAdapter);

    }

    private void populateUi() {

        Account account = new Account();
        account.setLegalName("DMAC Customer Account");
        account.setShortName("DMAC");
        account.setCustomerCode("0091");
        account.setGstrNumber("12340912");
        account.setPanNumber("CMPV2323C");
        account.setAccountStatus("New");
        account.setCreatedDate("2018-12-05 00:19:31");
        account.setUpdatedDate("2018-12-05 00:19:31");
        account.setCreatedBy("FE DEMAC");

        accountsList.add(account);
        accountsList.add(account);
        accountsList.add(account);
        accountsList.add(account);
        accountsList.add(account);
        accountsList.add(account);

        accountListAdapter.notifyDataSetChanged();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountsInteractionListener) {
            mListener = (OnAccountsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAccountsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAccountsInteractionListener {

    }
}
