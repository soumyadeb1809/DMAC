package in.teamconsultants.dmac.ui.home.jobs;

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
import in.teamconsultants.dmac.model.CustomerJob;
import in.teamconsultants.dmac.model.JobUploadFile;

public class CustomerJobsFragment extends Fragment {

    private OnCustomerJobsFragmentInteractionListener mListener;

    private RecyclerView rvCustomerJobs;

    private ArrayList<CustomerJob> customerJobsList;
    private CustomerJobsAdapter customerJobsAdapter;

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

        initializeViews(v);

        initDummyData();

        return v;
    }


    private void initializeViews(View v) {

        rvCustomerJobs = v.findViewById(R.id.rv_customer_jobs);

        customerJobsList = new ArrayList<>();
        customerJobsAdapter = new CustomerJobsAdapter(getContext(), customerJobsList);

        rvCustomerJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerJobs.setAdapter(customerJobsAdapter);

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

    private void initDummyData() {


        CustomerJob customerJob1 = new CustomerJob();
        customerJob1.setJobName("DMAC_04012019_115");
        customerJob1.setAccountName("DMAC");
        customerJob1.setEndCustomer("Customer DMAC");
        customerJob1.setJobStatus("In progress");
        customerJob1.setCreateDate("2019-01-04 08:30:58");
        customerJob1.setUpdateDate("2019-01-04 08:30:58");
        customerJob1.setJobUrl("https://staging.teamconsultants.in/dmac/Job/DownloadJob/N2hjVDVSNnRXd3crUXdhWGE0ZWttUT09");

        ArrayList<JobUploadFile> cb1FilesList = new ArrayList<>();
        JobUploadFile cb1File1 = new JobUploadFile();
        cb1File1.setFileNo(1);
        cb1File1.setFileCategory("Sale Invoices");
        cb1File1.setFileType("GST invoices");
        cb1File1.setFileUrl("https://staging.teamconsultants.in/upload/account//job/job_file_115_0_1546570858.jpg");
        cb1File1.setFileStatus("Validation Completed");
        cb1File1.setUploadAgain(false);
        cb1File1.setFileNotes("Some notes goes here for the uploaded file...");
        cb1FilesList.add(cb1File1);

        JobUploadFile cb1File2 = new JobUploadFile();
        cb1File2.setFileNo(2);
        cb1File2.setFileCategory("Others");
        cb1File2.setFileType("Other Bills");
        cb1File2.setFileUrl("https://staging.teamconsultants.in/upload/account//job/job_file_115_1_1546570858.jpg");
        cb1File2.setFileStatus("Validation Failed");
        cb1File2.setUploadAgain(true);
        cb1File2.setFileNotes("Some notes goes here for the uploaded file...");
        cb1FilesList.add(cb1File2);

        customerJob1.setJobUploadFiles(cb1FilesList);

        customerJobsList.add(customerJob1);

        customerJobsAdapter.notifyDataSetChanged();

        CustomerJob customerJob2 = new CustomerJob();
        customerJob2.setJobName("DMAC_03012019_113");
        customerJob2.setAccountName("DMAC");
        customerJob2.setEndCustomer("Customer DMAC");
        customerJob2.setJobStatus("In progress");
        customerJob2.setCreateDate("2019-01-03 10:59:27");
        customerJob2.setUpdateDate("2019-01-04 13:44:23");
        customerJob2.setJobUrl("https://staging.teamconsultants.in/dmac/Job/DownloadJob/aDJlR0Uzc2dsTUJKM3UvdnpMVEkwUT09");

        ArrayList<JobUploadFile> cb2FilesList = new ArrayList<>();
        JobUploadFile cb2File1 = new JobUploadFile();
        cb2File1.setFileNo(1);
        cb2File1.setFileCategory("Sale Invoices");
        cb2File1.setFileType("GST invoices");
        cb2File1.setFileUrl("https://staging.teamconsultants.in/upload/account//job/job_file_113_0_1546493367.jpg");
        cb2File1.setFileStatus("Validation Failed");
        cb2File1.setUploadAgain(true);
        cb2File1.setFileNotes("Some notes goes here for the uploaded file...");
        cb2FilesList.add(cb2File1);

        JobUploadFile cb2File2 = new JobUploadFile();
        cb2File2.setFileNo(2);
        cb2File2.setFileCategory("Purchase Bills");
        cb2File2.setFileType("Raw Material Bills");
        cb2File2.setFileUrl("https://staging.teamconsultants.in/upload/account//job/job_file_113_1_1546493367.jpg");
        cb2File2.setFileStatus("Data Entry Completed");
        cb2File2.setUploadAgain(false);
        cb2File2.setFileNotes("Some notes goes here for the uploaded file...");
        cb2FilesList.add(cb2File2);

        customerJob2.setJobUploadFiles(cb2FilesList);

        customerJobsList.add(customerJob2);

        customerJobsList.add(customerJob1);
        customerJobsList.add(customerJob2);
        customerJobsList.add(customerJob1);
        customerJobsList.add(customerJob2);
        customerJobsList.add(customerJob1);
        customerJobsList.add(customerJob2);
        customerJobsList.add(customerJob1);
        customerJobsList.add(customerJob2);

        customerJobsAdapter.notifyDataSetChanged();

    }
}
