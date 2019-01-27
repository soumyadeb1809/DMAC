package in.teamconsultants.dmac.ui.home.jobs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CreateJobResponse;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.model.FileSearchResponse;
import in.teamconsultants.dmac.model.JobUploadFile;
import in.teamconsultants.dmac.model.StatusObj;
import in.teamconsultants.dmac.model.StatusResponse;
import in.teamconsultants.dmac.network.ApiClient;
import in.teamconsultants.dmac.network.ApiInterface;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CustomerJobsFragment extends Fragment {

    private OnCustomerJobsFragmentInteractionListener mListener;

    private RecyclerView rvCustomerJobs;
    private ImageButton btnSearch;
    private RelativeLayout grpNoResult;
    private Button btnClearSearch;
    private Button btnEditSearch;

    private ArrayList<CustomerJob> customerJobsList;
    private CustomerJobsAdapter customerJobsAdapter;
    private String[] fileStatusArr;
    private SimpleSpinnerAdapter fileStatusSpinnerAdapter;
    private AlertDialog searchDialog;

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private String token;
    private HashMap<String, String> statusMap;

    public CustomerJobsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_customer_jobs, container, false);

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(getContext());

        getFileStatusList(v);


        return v;
    }


    private void initializeViews(View v) {

        rvCustomerJobs = v.findViewById(R.id.rv_customer_jobs);
        btnSearch = v.findViewById(R.id.btn_search_file);
        btnClearSearch = v.findViewById(R.id.btn_clear_search);
        btnEditSearch = v.findViewById(R.id.btn_edit_search);
        grpNoResult = v.findViewById(R.id.grp_no_result);

        setUpSearchAlert();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog.show();
            }
        });

        btnEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog.show();
            }
        });

        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Loading files...");
                progress.setCancelable(false);
                progress.show();
                HashMap<String, Object> searchQuery = new HashMap<>();
                searchFiles(searchQuery);
            }
        });

    }

    private void setUpSearchAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_search_file, null);
        builder.setView(dialogLayout);


        final TextView fileCreatedStartDt = dialogLayout.findViewById(R.id.file_created_start_dt);
        final TextView fileCreatedEndDt = dialogLayout.findViewById(R.id.file_created_end_dt);
        final TextView lastUpdatedStartDt = dialogLayout.findViewById(R.id.last_updated_start_dt);
        final TextView lastUpdatedEndDt = dialogLayout.findViewById(R.id.last_updated_end_dt);
        final Spinner fileStatusSpinner = dialogLayout.findViewById(R.id.spinner_file_status);
        final EditText etFileName = dialogLayout.findViewById(R.id.et_file_name);

        fileStatusSpinnerAdapter = new SimpleSpinnerAdapter(getContext(), R.layout.spinner_layout, fileStatusArr);
        fileStatusSpinner.setAdapter(fileStatusSpinnerAdapter);

        fileCreatedStartDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showDatePicker(fileCreatedStartDt);
            }
        });
        fileCreatedEndDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(fileCreatedEndDt);
            }
        });
        lastUpdatedStartDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(lastUpdatedStartDt);
            }
        });
        lastUpdatedEndDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(lastUpdatedEndDt);
            }
        });


        builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileCreatedFrom = fileCreatedStartDt.getText().toString();
                String fileCreatedTo = fileCreatedEndDt.getText().toString();
                String fileUpdatedFrom = lastUpdatedStartDt.getText().toString();
                String fileUpdatedTo = lastUpdatedEndDt.getText().toString();
                String fileName = etFileName.getText().toString();

                String statusSelection = fileStatusSpinner.getSelectedItem().toString();
                int statusId = -1;

                for(String key: statusMap.keySet()){
                    if(statusSelection.equals(statusMap.get(key))){
                        statusId = Integer.parseInt(key);
                    }
                }

                HashMap<String, Object> searchQuery = new HashMap<>();

                if (!fileCreatedFrom.equals(AppConstants.FILE_SEARCH.INVALID_START_DATE)){
                    searchQuery.put("JobCreatedFrom", fileCreatedFrom);
                }
                if (!fileCreatedTo.equals(AppConstants.FILE_SEARCH.INVALID_END_DATE)){
                    searchQuery.put("JobCreatedTo", fileCreatedFrom);
                }
                if (!fileUpdatedFrom.equals(AppConstants.FILE_SEARCH.INVALID_START_DATE)){
                    searchQuery.put("JobUpdatedFrom", fileCreatedFrom);
                }
                if (!fileUpdatedTo.equals(AppConstants.FILE_SEARCH.INVALID_END_DATE)){
                    searchQuery.put("JobUpdatedTo", fileCreatedFrom);
                }
                if(!TextUtils.isEmpty(fileName)){
                    searchQuery.put("FileName", fileName);
                }
                if(statusId != -1){
                    searchQuery.put("StatusId", statusId);
                }

                Log.d(AppConstants.LOG_TAG, "query: " + gson.toJson(searchQuery));

                progress.setMessage("Searching files...");
                progress.setCancelable(false);
                progress.show();

                searchFiles(searchQuery);

            }
        });
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchDialog.dismiss();
            }
        });
        searchDialog = builder.create();

    }

    private void showDatePicker(final TextView tvDate) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvDate.setText(addZeroB4DtElem(dayOfMonth)+"-" +addZeroB4DtElem((monthOfYear+1))+"-"+year);

                    }
                }, year, month, day);

        datePickerDialog.show();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCustomerJobsFragmentInteractionListener) {
            mListener = (OnCustomerJobsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCustomerJobsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCustomerJobsFragmentInteractionListener {

    }

    private void searchFiles(Map<String, Object> searchQueries) {

        Call<FileSearchResponse> apiCall = apiInterface.doSearchFiles(Utility.getHeader(token), searchQueries);

        apiCall.enqueue(new Callback<FileSearchResponse>() {
            @Override
            public void onResponse(Call<FileSearchResponse> call, Response<FileSearchResponse> response) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                FileSearchResponse fileSearchResponse = response.body();

                customerJobsList = (ArrayList<CustomerJob>)fileSearchResponse.getSearchResultList();
                customerJobsAdapter = new CustomerJobsAdapter(getContext(), customerJobsList, statusMap);

                rvCustomerJobs.setLayoutManager(new LinearLayoutManager(getContext()));
                rvCustomerJobs.setAdapter(customerJobsAdapter);

                if(customerJobsList.size() == 0){
                    grpNoResult.setVisibility(View.VISIBLE);
                }
                else {
                    grpNoResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<FileSearchResponse> call, Throwable t) {
                progress.dismiss();
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                Utility.showAlert(getContext(), "Error", "Something went wrong, please try again");
            }
        });

    }


    private void getFileStatusList(final View v) {

        progress.setMessage("Loading files...");
        progress.setCancelable(false);
        progress.show();

        String type = "File Status";
        Call<StatusResponse> statusResponseCall = apiInterface.doGetStatus(Utility.getHeader(token), type);

        statusResponseCall.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                StatusResponse statusResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "response-type: "+ gson.toJson(statusResponse));
                statusMap = new HashMap<>();
                fileStatusArr = new String[statusResponse.getStatusList().size()];
                for (int i = 0; i < statusResponse.getStatusList().size(); i++) {
                    StatusObj statusObj = statusResponse.getStatusList().get(i);
                    statusMap.put(statusObj.getSId(), statusObj.getShortName());
                    fileStatusArr[i] = statusObj.getShortName();
                }
                initializeViews(v);
                searchFiles(new HashMap<String, Object>());
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                progress.dismiss();
            }
        });

    }

    public String addZeroB4DtElem(int dateElement){
        return String.valueOf((dateElement)<10 ? "0"+(dateElement) : (dateElement));
    }
}
