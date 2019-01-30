package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerDashboardFragment;
import in.teamconsultants.dmac.ui.home.jobs.CustomerJobsFragment;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.ui.home.profile.ProfileFragment;

import in.teamconsultants.dmac.R;

public class CustomerHomeActivity extends AppCompatActivity implements CustomerDashboardFragment.OnCustomerDashboardFragmentInteractionListener,
        CustomerJobsFragment.OnCustomerJobsFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener {

    private BottomNavigationView navigation;

    private CustomerDashboardFragment customerDashboardFragment;
    private CustomerJobsFragment customerJobsFragment;
    private ProfileFragment profileFragment;

    private Toolbar toolbar;

    private LinearLayout grpNewJob;

    {
        customerDashboardFragment = new CustomerDashboardFragment();
        customerJobsFragment = new CustomerJobsFragment();
        profileFragment = new ProfileFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() != navigation.getSelectedItemId()) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerDashboardFragment).commit();
                        grpNewJob.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.navigation_my_jobs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerJobsFragment).commit();
                        grpNewJob.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        return true;
                }
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

       /* toolbar = findViewById(R.id.toolbar); */

        navigation.setSelectedItemId(R.id.navigation_dashboard);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerDashboardFragment).commit();

        setSupportActionBar(toolbar);

        /*getSupportActionBar().setTitle("DMAC");*/

        initializeUi();

        setOnClickListeners();

    }

    private void initializeUi() {
        grpNewJob = findViewById(R.id.grp_new_job);

    }


    private void setOnClickListeners() {

        grpNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, NewJobActivity.class));
            }
        });

    }


    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

}
