package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FileCategoryCount;
import in.teamconsultants.dmac.network.dto.FileCategoryWiseCountResponse;
import in.teamconsultants.dmac.network.dto.FileCountResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDashboardFragment extends Fragment {


    private OnCustomerDashboardFragmentInteractionListener mListener;

    private TextView tvTotalFiles, tvVerifiedFiles;

    private RecyclerView rvDashboard;

    private ApiInterface apiInterface;
    private ProgressDialog progress;
    private SharedPreferences spUserData;
    private String token;
    private Map<String, String> headerMap;
    private Gson gson;
    private View v;

    private List<FileCategoryCount> fileCategoryCountList;
    private DashboardAdapter dashboardAdapter;

    private ImageView btnHelp;

    public CustomerDashboardFragment() {
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
        v = inflater.inflate(R.layout.fragment_customer_dashboard, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progress = new ProgressDialog(getContext());
        spUserData = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);
        token = spUserData.getString(AppConstants.SP.TAG_TOKEN, "");
        headerMap = new HashMap<>();
        headerMap.put("TOKEN", token);

        gson = new Gson();

        //initializeUi(v);

        loadData();

        return v;
    }

    private void initializeUi() {

      /*  tvTotalFiles = v.findViewById(R.id.txt_total_files);
        tvVerifiedFiles = v.findViewById(R.id.txt_verified_files);
*/
  //      btnHelp = v.findViewById(R.id.btn_help);

 /*       btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showAlert(getContext(), "Support", "You can contact us on\nPhone: 9876543210\nEmail: support@dmac.com");
            }
        });*/

        rvDashboard = v.findViewById(R.id.rv_dashboard);

        if(fileCategoryCountList == null){
            fileCategoryCountList = new ArrayList<>();
        }

        dashboardAdapter = new DashboardAdapter(getContext(), fileCategoryCountList);
        rvDashboard.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDashboard.setAdapter(dashboardAdapter);

    }


    private void loadData() {
/*
        String totalFiles = spUserData.getString(AppConstants.SP.TAG_TOTAL_FILES, null);
        String verifiedFiles = spUserData.getString(AppConstants.SP.TAG_VERIFIED_FILES, null);

        final SharedPreferences.Editor spEditor = spUserData.edit();

        if(null == totalFiles || null == verifiedFiles){
            progress.setMessage("Loading data...");
            progress.setCancelable(false);
            progress.show();

            tvVerifiedFiles.setText("NA");
            tvTotalFiles.setText("NA");
        }
        else {

            tvTotalFiles.setText(totalFiles);
            tvVerifiedFiles.setText(verifiedFiles);

        }*/

  /*      Map<String, String> queries = new HashMap<>();

        Call<FileCountResponse> fileCountResponseCall = apiInterface.doGetFileCount(headerMap, queries);
        fileCountResponseCall.enqueue(new Callback<FileCountResponse>() {
            @Override
            public void onResponse(Call<FileCountResponse> call, Response<FileCountResponse> response) {
                Log.d(AppConstants.LOG_TAG, "fileCountResponseCall: "+gson.toJson(response.body()));
                FileCountResponse fileCountResponse = response.body();

                tvTotalFiles.setText(String.valueOf(fileCountResponse.getFileCount()));
                spEditor.putString(AppConstants.SP.TAG_TOTAL_FILES, String.valueOf(fileCountResponse.getFileCount()));
                spEditor.commit();

            }

            @Override
            public void onFailure(Call<FileCountResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getActivity(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });

        queries.clear();

        queries.put("StatusId", "9");
        Call<FileCountResponse> verifiedFilesResponseCall = apiInterface.doGetFileCount(headerMap, queries);
        verifiedFilesResponseCall.enqueue(new Callback<FileCountResponse>() {
            @Override
            public void onResponse(Call<FileCountResponse> call, Response<FileCountResponse> response) {
                Log.d(AppConstants.LOG_TAG, "fileCountResponseCall: "+gson.toJson(response.body()));
                FileCountResponse fileCountResponse = response.body();

                tvVerifiedFiles.setText(String.valueOf(fileCountResponse.getFileCount()));
                spEditor.putString(AppConstants.SP.TAG_VERIFIED_FILES, String.valueOf(fileCountResponse.getFileCount()));
                spEditor.commit();
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<FileCountResponse> call, Throwable t) {
                progress.dismiss();
                Utility.showAlert(getActivity(), "Error", "An unknown error occurred, please try again");
                Log.d(AppConstants.LOG_TAG, "FAILED: " + t.getMessage());
                t.printStackTrace();
            }
        });*/


        progress.setMessage("Loading bills data...");
        progress.setCancelable(false);
        progress.show();

        Call<FileCategoryWiseCountResponse> categoryWiseCountCall = apiInterface.doGetFileCategoryCount(token);

        categoryWiseCountCall.enqueue(new Callback<FileCategoryWiseCountResponse>() {
            @Override
            public void onResponse(Call<FileCategoryWiseCountResponse> call, Response<FileCategoryWiseCountResponse> response) {
                progress.dismiss();
                FileCategoryWiseCountResponse fileCategoryWiseCountResponse = response.body();
                fileCategoryCountList = fileCategoryWiseCountResponse.getFileCategoryCount();
                initializeUi();
            }

            @Override
            public void onFailure(Call<FileCategoryWiseCountResponse> call, Throwable t) {
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
        if (context instanceof OnCustomerDashboardFragmentInteractionListener) {
            mListener = (OnCustomerDashboardFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCustomerDashboardFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCustomerDashboardFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
