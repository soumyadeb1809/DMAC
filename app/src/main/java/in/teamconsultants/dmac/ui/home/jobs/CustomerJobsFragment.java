package in.teamconsultants.dmac.ui.home.jobs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.network.dto.FileSearchResponse;
import in.teamconsultants.dmac.model.StatusObj;
import in.teamconsultants.dmac.network.dto.StatusResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
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
    private TextView tvMyFiles;
    private LinearLayout grpBack;
    private SwipeRefreshLayout swipeRefresh;

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

    boolean isSearched = false;

    private View v;

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
        v = inflater.inflate(R.layout.fragment_customer_jobs, container, false);

        // Initialize API Interface:
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(getContext());

        initializeViews();
        getFileStatusList();



        return v;
    }


    private void initializeViews() {

        rvCustomerJobs = v.findViewById(R.id.rv_customer_jobs);
        btnSearch = v.findViewById(R.id.btn_search_file);
        btnClearSearch = v.findViewById(R.id.btn_clear_search);
        btnEditSearch = v.findViewById(R.id.btn_edit_search);
        grpNoResult = v.findViewById(R.id.grp_no_result);
        tvMyFiles = v.findViewById(R.id.title_my_files);
        grpBack = v.findViewById(R.id.grp_back);
        swipeRefresh = v.findViewById(R.id.swipe_refresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                getFileStatusList();
            }
        });

        if(FilesActivity.categoryName != null && !TextUtils.isEmpty(FilesActivity.categoryName)) {
            tvMyFiles.setText(FilesActivity.categoryName);
        }
        else {
            tvMyFiles.setText("My Files");
        }

        //setUpSearchAlert();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearched) {
                    searchDialog.show();
                }
                else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setMessage("Do you want to clear the search or edit existing search?");
                    alertBuilder.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress.setMessage("Loading files...");
                            progress.setCancelable(false);
                            progress.show();
                            HashMap<String, Object> searchQuery = new HashMap<>();
                            searchFiles(searchQuery);
                            isSearched = false;
                        }
                    });
                    alertBuilder.setNegativeButton("EDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchDialog.show();
                        }
                    });
                    alertBuilder.show();
                }
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
                isSearched = false;
            }
        });

        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
                 Utility.showDatePicker(getContext(),fileCreatedStartDt);
            }
        });
        fileCreatedEndDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePicker(getContext(), fileCreatedEndDt);
            }
        });
        lastUpdatedStartDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePicker(getContext(), lastUpdatedStartDt);
            }
        });
        lastUpdatedEndDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePicker(getContext(), lastUpdatedEndDt);
            }
        });


        builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    Date fileCreatedFromObj = dateFormat.parse(fileCreatedStartDt.getText().toString());
                    Date fileCreatedObj = dateFormat.parse(fileCreatedEndDt.getText().toString());
                    Date fileUpdatedFromObj = dateFormat.parse(lastUpdatedStartDt.getText().toString());
                    Date fileUpdatedToObj = dateFormat.parse(lastUpdatedEndDt.getText().toString());

                    SimpleDateFormat formatToSend = new SimpleDateFormat("yyyy-MM-dd");



                    String fileCreatedFrom =  formatToSend.format(fileCreatedFromObj);
                    String fileCreatedTo = formatToSend.format(fileCreatedObj);
                    String fileUpdatedFrom = formatToSend.format(fileUpdatedFromObj);
                    String fileUpdatedTo = formatToSend.format(fileUpdatedToObj);

                    String fileName = etFileName.getText().toString();

                    String statusSelection = fileStatusSpinner.getSelectedItem().toString();
                    int statusId = -1;

                    for (String key : statusMap.keySet()) {
                        if (statusSelection.equals(statusMap.get(key))) {
                            statusId = Integer.parseInt(key);
                        }
                    }

                    HashMap<String, Object> searchQuery = new HashMap<>();

                    if (!fileCreatedFrom.equals(AppConstants.FILE_SEARCH.INVALID_START_DATE)) {
                        searchQuery.put("JobCreatedFrom", fileCreatedFrom);
                    }
                    if (!fileCreatedTo.equals(AppConstants.FILE_SEARCH.INVALID_END_DATE)) {
                        searchQuery.put("JobCreatedTo", fileCreatedTo);
                    }
                    if (!fileUpdatedFrom.equals(AppConstants.FILE_SEARCH.INVALID_START_DATE)) {
                        searchQuery.put("JobUpdatedFrom", fileUpdatedFrom);
                    }
                    if (!fileUpdatedTo.equals(AppConstants.FILE_SEARCH.INVALID_END_DATE)) {
                        searchQuery.put("JobUpdatedTo", fileUpdatedTo);
                    }
                    if (!TextUtils.isEmpty(fileName)) {
                        searchQuery.put("FileName", fileName);
                    }
                    if (statusId != -1) {
                        searchQuery.put("StatusId", statusId);
                    }

                    Log.d(AppConstants.LOG_TAG, "query: " + gson.toJson(searchQuery));

                    progress.setMessage("Searching files...");
                    progress.setCancelable(false);
                    progress.show();

                    searchFiles(searchQuery);
                    isSearched = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                    Utility.showAlert(getActivity(), "Info", "Invalid date input");
                    return;
                }


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

                Log.d(AppConstants.LOG_TAG, "response: "+ gson.toJson(response.body()));
                FileSearchResponse fileSearchResponse = response.body();
                if(fileSearchResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)) {
                    ArrayList<CustomerJob> resultList = (ArrayList<CustomerJob>) fileSearchResponse.getSearchResultList();

                    customerJobsList = new ArrayList<>();

                    if (FilesActivity.categoryName != null && !TextUtils.isEmpty(FilesActivity.categoryName)) {
                        for (CustomerJob customerJob : resultList) {
                            if (customerJob.getFileCategory().equals(FilesActivity.categoryName)) {
                                customerJobsList.add(customerJob);
                            }
                        }
                    }

                    progress.dismiss();

                    customerJobsAdapter = new CustomerJobsAdapter(getContext(), customerJobsList, statusMap);

                    rvCustomerJobs.setLayoutManager(new LinearLayoutManager(getContext()));
                    rvCustomerJobs.setAdapter(customerJobsAdapter);

                    if (customerJobsList.size() == 0) {
                        grpNoResult.setVisibility(View.VISIBLE);
                    } else {
                        grpNoResult.setVisibility(View.GONE);
                    }
                }
                else {
                    //TODO: Handle token invalid case
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


    private void getFileStatusList() {

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
                setUpSearchAlert();
                searchFiles(new HashMap<String, Object>());
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                progress.dismiss();
            }
        });

    }

}
