package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerDashboardFragment;
import in.teamconsultants.dmac.ui.home.jobs.CustomerJobsFragment;
import in.teamconsultants.dmac.ui.home.profile.ProfileFragment;

import in.teamconsultants.dmac.R;

public class CustomerHomeActivity extends AppCompatActivity implements CustomerDashboardFragment.OnCustomerDashboardFragmentInteractionListener,
        CustomerJobsFragment.OnCustomerJobsFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener {

    private BottomNavigationView navigation;

    private CustomerDashboardFragment customerDashboardFragment;
    private CustomerJobsFragment customerJobsFragment;
    private ProfileFragment profileFragment;

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
                        return true;
                    case R.id.navigation_my_jobs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerJobsFragment).commit();
                        return true;

                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
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

        navigation.setSelectedItemId(R.id.navigation_dashboard);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerDashboardFragment).commit();



    }




    /*
     * Fragment Interaction Listeners
     */
    @Override
    public void onCustomerDashboardFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCustomerJobsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }
}
