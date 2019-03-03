package in.teamconsultants.dmac.ui.home.dashboard.fe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.network.dto.AccountCountResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeDashboardFragment extends Fragment {

    private OnFeDashboardInteractionListener mListener;

    private TextView tvTotalAccounts, tvOpenAccounts;

    private ApiInterface apiInterface;
    private ProgressDialog progress;
    private SharedPreferences spUserData;
    private String token;
    private Map<String, String> headerMap;
    private Gson gson;
    private ImageView btnHelp;

    private SwipeRefreshLayout swipeRefresh;


    public FeDashboardFragment() {
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
        View v = inflater.inflate(R.layout.fragment_fe_dashboard, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progress = new ProgressDialog(getContext());
        spUserData = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);
        token = spUserData.getString(AppConstants.SP.TAG_TOKEN, "");
        headerMap = new HashMap<>();
        headerMap.put("TOKEN", token);

        gson = new Gson();


        initializeUi(v);

        loadData();

        return v;
    }


    private void initializeUi(View v) {

        tvOpenAccounts = v.findViewById(R.id.txt_open_accounts);
        tvTotalAccounts = v.findViewById(R.id.txt_total_accounts);
        btnHelp = v.findViewById(R.id.btn_help);
        swipeRefresh = v.findViewById(R.id.swipe_refresh);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showAlert(getContext(), "Support", "You can contact us on\nPhone: 9876543210\nEmail: support@dmac.com");
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                loadData();
            }
        });

    }


    private void loadData() {

        String totalAccounts = spUserData.getString(AppConstants.SP.TAG_TOTAL_ACCOUNTS, null);
        String openAccounts = spUserData.getString(AppConstants.SP.TAG_OPEN_ACCOUNTS, null);

        final SharedPreferences.Editor spEditor = spUserData.edit();

        if(null == totalAccounts || null == openAccounts){
            progress.setMessage("Loading data...");
            progress.setCancelable(false);
            progress.show();

            tvTotalAccounts.setText("NA");
            tvOpenAccounts.setText("NA");
        }
        else {

            tvTotalAccounts.setText(totalAccounts);
            tvOpenAccounts.setText(openAccounts);

        }

        Map<String, String> queries = new HashMap<>();

        Call<AccountCountResponse> accountCountResponseCall = apiInterface.doGetAccountCount(headerMap, queries);
        accountCountResponseCall.enqueue(new Callback<AccountCountResponse>() {
            @Override
            public void onResponse(Call<AccountCountResponse> call, Response<AccountCountResponse> response) {
                Log.d(AppConstants.LOG_TAG, "accountCountResponseCall: "+gson.toJson(response.body()));
                AccountCountResponse accountCountResponse = response.body();
                if (accountCountResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    tvTotalAccounts.setText(String.valueOf(accountCountResponse.getAccountCount()));
                    spEditor.putString(AppConstants.SP.TAG_TOTAL_ACCOUNTS, String.valueOf(accountCountResponse.getAccountCount()));
                    spEditor.commit();
                }
                else {
                    Utility.forceLogoutUser(getActivity());
                }

            }

            @Override
            public void onFailure(Call<AccountCountResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getActivity(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

        queries.clear();

        queries.put("StatusId", "19");
        Call<AccountCountResponse> verifiedFilesResponseCall = apiInterface.doGetAccountCount(headerMap, queries);
        verifiedFilesResponseCall.enqueue(new Callback<AccountCountResponse>() {
            @Override
            public void onResponse(Call<AccountCountResponse> call, Response<AccountCountResponse> response) {
                Log.d(AppConstants.LOG_TAG, "accountCountResponseCall: "+gson.toJson(response.body()));
                AccountCountResponse accountCountResponse = response.body();

                if(accountCountResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    tvOpenAccounts.setText(String.valueOf(accountCountResponse.getAccountCount()));
                    spEditor.putString(AppConstants.SP.TAG_OPEN_ACCOUNTS, String.valueOf(accountCountResponse.getAccountCount()));
                    spEditor.commit();
                    progress.dismiss();
                }
                else {
                    Utility.forceLogoutUser(getActivity());
                }
            }

            @Override
            public void onFailure(Call<AccountCountResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getActivity(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFeDashboardInteractionListener) {
            mListener = (OnFeDashboardInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFeDashboardInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFeDashboardInteractionListener {

    }
}
