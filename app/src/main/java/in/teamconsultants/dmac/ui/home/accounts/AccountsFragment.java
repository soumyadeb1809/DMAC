package in.teamconsultants.dmac.ui.home.accounts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.Account;
import in.teamconsultants.dmac.network.dto.AccountSearchResponse;
import in.teamconsultants.dmac.model.AccountSearchResultObj;
import in.teamconsultants.dmac.model.StatusObj;
import in.teamconsultants.dmac.network.dto.StatusResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountsFragment extends Fragment {

    private OnAccountsInteractionListener mListener;

    private ArrayList<Account> accountsList;
    private RecyclerView rvAccountsList;

    private ArrayList<AccountSearchResultObj> accountSearchResultList;

    private AccountListAdapter accountListAdapter;

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String token;

    private HashMap<String, String> statusMap;

    private View v;

    private SwipeRefreshLayout swipeRefresh;

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
        v = inflater.inflate(R.layout.fragment_accounts, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progress = new ProgressDialog(getContext());

        gson = new Gson();

        sharedPreferences = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);

        accountSearchResultList = new ArrayList<>();

        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");

        initializeUi();

        getSearchResults();

        //populateUi();

        return v;
    }

    private void initializeUi() {

        rvAccountsList = v.findViewById(R.id.rv_accounts_list);
        swipeRefresh = v.findViewById(R.id.swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                getSearchResults();
            }
        });
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

    private void getSearchResults() {
        progress.setMessage("Fetching accounts...");
        progress.setCancelable(false);
        progress.show();
        Call<AccountSearchResponse> accountSearchResultCall = apiInterface.doAccountSearch(Utility.getHeader(token));

        accountSearchResultCall.enqueue(new Callback<AccountSearchResponse>() {
            @Override
            public void onResponse(Call<AccountSearchResponse> call, Response<AccountSearchResponse> response) {
                AccountSearchResponse accountSearchResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(accountSearchResponse));

                if(accountSearchResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    accountSearchResultList = accountSearchResponse.getSearchResultList();

                    getAccountStatusList();
                }
                else {
                    Utility.forceLogoutUser(getActivity());
                }
            }

            @Override
            public void onFailure(Call<AccountSearchResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
            }
        });

    }

    private void getAccountStatusList() {

        String type = "Account Status";
        Call<StatusResponse> statusResponseCall = apiInterface.doGetStatus(Utility.getHeader(token), type);

        statusResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                progress.dismiss();
                StatusResponse statusResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(statusResponse));
                if(statusResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    statusMap = new HashMap<>();
                    for (StatusObj statusObj : statusResponse.getStatusList()) {
                        statusMap.put(statusObj.getSId(), statusObj.getShortName());
                    }

                    accountsList = new ArrayList<>();
                    accountListAdapter = new AccountListAdapter(getContext(), accountSearchResultList, statusMap);

                    rvAccountsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvAccountsList.setAdapter(accountListAdapter);
                }
                else {
                    Utility.forceLogoutUser(getActivity());
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                progress.dismiss();
            }
        });

    }

}
