package in.teamconsultants.dmac.ui.home.reports;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.network.dto.CreateReportResponse;
import in.teamconsultants.dmac.model.Report;
import in.teamconsultants.dmac.model.ReportType;
import in.teamconsultants.dmac.network.dto.ReportTypeResponse;
import in.teamconsultants.dmac.network.dto.ReportsResponse;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.ui.home.spinner.SimpleSpinnerAdapter;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReportsFragment extends Fragment {


    private View v;

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private String token;

    private String[] reportTypesArr;
    private Map<String, String> reportTypeMap;
    private List<ReportType> reportTypeList;

    private List<Report> reportList;
    private RecyclerView rvReports;

    private Spinner spinReportType;
    private SwipeRefreshLayout swipeRefresh;

    private TextView tvStartDate, tvEndDate;
    private LinearLayout grpReqReport;



    public ReportsFragment() {
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
        v = inflater.inflate(R.layout.fragment_reports, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        gson = new Gson();
        sharedPreferences = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(getContext());

        initializeUi();

        fetchReportTypes();

        return v;
    }


    private void fetchReportTypes() {

        progress.setMessage("Loading report...");
        progress.setCancelable(false);
        progress.show();

        Call<ReportTypeResponse> reportTypeCall = apiInterface.doGetReportTypes(token);

        reportTypeCall.enqueue(new Callback<ReportTypeResponse>() {
            @Override
            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                ReportTypeResponse reportTypeResponse = response.body();
                Log.d(AppConstants.LOG_TAG, "reportTypeResponse: " + gson.toJson(reportTypeResponse));

                reportTypeList = reportTypeResponse.getReport_type_list();
                reportTypesArr = new String[reportTypeList.size()];
                reportTypeMap = new HashMap<>();
                for(int i = 0; i < reportTypeList.size(); i++){
                    reportTypesArr[i] = reportTypeList.get(i).getRt_name();
                    reportTypeMap.put(reportTypeList.get(i).getRt_id(), reportTypeList.get(0).getRt_name());
                }

                //progress.dismiss();
                SimpleSpinnerAdapter reportTypeAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.spinner_layout_small, reportTypesArr);
                spinReportType.setAdapter(reportTypeAdapter);

                fetchReports();

            }

            @Override
            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                progress.dismiss();
            }
        });

    }


    private void fetchReports(){

        /*progress.setMessage("Loading reports...");
        progress.setCancelable(false);
        progress.show();
*/
        Call<ReportsResponse> reportsCall = apiInterface.doGetReportList(token);
        reportsCall.enqueue(new Callback<ReportsResponse>() {
            @Override
            public void onResponse(Call<ReportsResponse> call, Response<ReportsResponse> response) {

                progress.dismiss();
                ReportsResponse reportsResponse = response.body();
                reportList = reportsResponse.getSearchResultList();

                ReportsAdapter reportsAdapter = new ReportsAdapter(getActivity() , (ArrayList<Report>) reportList);
                rvReports.setLayoutManager(new LinearLayoutManager(getContext()));
                rvReports.setAdapter(reportsAdapter);
                rvReports.setNestedScrollingEnabled(false);


            }

            @Override
            public void onFailure(Call<ReportsResponse> call, Throwable t) {
                Log.d(AppConstants.LOG_TAG, "FAILED: "+t.getMessage());
                progress.dismiss();
            }
        });
    }

    private void initializeUi() {

        spinReportType = v.findViewById(R.id.spinner_req_report);
        rvReports = v.findViewById(R.id.rv_reports);
        tvStartDate = v.findViewById(R.id.txt_start_dt);
        tvEndDate = v.findViewById(R.id.txt_end_dt);
        grpReqReport = v.findViewById(R.id.btn_req_report);
        swipeRefresh = v.findViewById(R.id.swipe_refresh);

        setOnClickListeners();

    }

    private void setOnClickListeners() {

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePicker(getContext(), tvStartDate);
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDatePicker(getContext(), tvEndDate);
            }
        });

        grpReqReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserInput();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                fetchReportTypes();
            }
        });

    }

    private void validateUserInput() {

        String startDate = tvStartDate.getText().toString();
        String endDate = tvEndDate.getText().toString();

        if(startDate.equals("Start Date") || endDate.equals("End Date")){
            Utility.showAlert(getActivity(), "Info", "Please select a valid date range");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date startDateObj = dateFormat.parse(startDate);
            Date endDateObj = dateFormat.parse(endDate);

            SimpleDateFormat formatToSend = new SimpleDateFormat("yyyy-MM-dd");
            startDate = formatToSend.format(startDateObj);
            endDate = formatToSend.format(endDateObj);

        }
        catch(Exception e){
            e.printStackTrace();
            Utility.showAlert(getActivity(), "Info", "Invalid date input");
            return;
        }

        startDataUpload(startDate, endDate);

    }

    private void startDataUpload(String startDate, String endDate) {

        progress.setMessage("Submitting request for report...");
        progress.setCancelable(false);
        progress.show();

        int reqTypeSpinnerPos = spinReportType.getSelectedItemPosition();
        String reportTypeId = reportTypeList.get(reqTypeSpinnerPos).getRt_id();

        Map<String, String> feildsMap = new HashMap<>();
        feildsMap.put("report_type_id", reportTypeId);
        feildsMap.put("report_from", startDate);
        feildsMap.put("report_to", endDate);

        Call<CreateReportResponse> createReportCall = apiInterface.doCreateReportRequest(token, feildsMap);

        createReportCall.enqueue(new Callback<CreateReportResponse>() {
            @Override
            public void onResponse(Call<CreateReportResponse> call, Response<CreateReportResponse> response) {
                progress.dismiss();
                CreateReportResponse createReportResponse = response.body();

                if(createReportResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){
                    tvStartDate.setText("Start Date");
                    tvEndDate.setText("End Date");
                    Utility.showAlert(getActivity(), "Success", "Request for report has been submitted successfully.");
                }
                else {
                    Utility.showAlert(getActivity(), "Failed", "Something went wrong, please try again");
                }

            }

            @Override
            public void onFailure(Call<CreateReportResponse> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Utility.showAlert(getActivity(), "Failed", "Something went wrong, please try again");
            }
        });

    }


}
