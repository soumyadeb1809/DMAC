package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FileCountResponse;
import in.teamconsultants.dmac.network.ApiClient;
import in.teamconsultants.dmac.network.ApiInterface;
import in.teamconsultants.dmac.ui.registration.RegularRegistrationActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDashboardFragment extends Fragment {


    private OnCustomerDashboardFragmentInteractionListener mListener;

    private TextView tvTotalFiles, tvVerifiedFiles;

    private ApiInterface apiInterface;
    private ProgressDialog progress;
    private SharedPreferences spUserData;
    private String token;
    private Map<String, String> headerMap;
    private Gson gson;

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
        View v = inflater.inflate(R.layout.fragment_customer_dashboard, container, false);

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

        tvTotalFiles = v.findViewById(R.id.txt_total_files);
        tvVerifiedFiles = v.findViewById(R.id.txt_verified_files);

    }


    private void loadData() {

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

        }

        Map<String, String> queries = new HashMap<>();

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
