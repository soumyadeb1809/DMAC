package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.ui.home.information.InformationFragment;
import in.teamconsultants.dmac.ui.home.jobs.CustomerJobsFragment;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.ui.home.profile.ProfileFragment;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.reports.ReportsFragment;
import in.teamconsultants.dmac.ui.notification.NotificationActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;

public class CustomerHomeActivity extends AppCompatActivity implements CustomerDashboardFragment.OnCustomerDashboardFragmentInteractionListener,
        CustomerJobsFragment.OnCustomerJobsFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;

    private CustomerDashboardFragment customerDashboardFragment;
    private CustomerJobsFragment customerJobsFragment;
    private ProfileFragment profileFragment;
    private ReportsFragment reportsFragment;
    private InformationFragment informationFragment;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private LinearLayout grpNotifications;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private LinearLayout grpNewJob;
    private LinearLayout grpNavToggle;
    private ImageView imgToggle;
    private LinearLayout grpLogout;

    private CircleImageView imgProfilePic;
    private TextView tvName, tvEmail;

    private boolean isDrawerOpen = false;

    private SharedPreferences sp;


    {
        customerDashboardFragment = new CustomerDashboardFragment();
        customerJobsFragment = new CustomerJobsFragment();
        profileFragment = new ProfileFragment();
        reportsFragment = new ReportsFragment();
        informationFragment = new InformationFragment();
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
                        toolbarTitle.setText("Dashboard");
                        return true;
/*
                    case R.id.navigation_my_jobs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerJobsFragment).commit();
                        grpNewJob.setVisibility(View.VISIBLE);
                        return true;
*/

                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        toolbarTitle.setText("Profile");
                        return true;

                    case R.id.navigation_reports:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reportsFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        toolbarTitle.setText("Reports");
                        return true;

                    case R.id.navigation_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, informationFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        toolbarTitle.setText("Information");
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

        toolbar = findViewById(R.id.toolbar);

        navigation.setSelectedItemId(R.id.navigation_dashboard);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, customerDashboardFragment).commit();

        setSupportActionBar(toolbar);

        toolbarTitle = toolbar.findViewById(R.id.tool_title);
        grpNotifications = toolbar.findViewById(R.id.grp_notification);
        grpNavToggle = toolbar.findViewById(R.id.grp_nav_toggle);
        imgToggle = toolbar.findViewById(R.id.img_toggle);

        refreshDrawerToggle();

        drawerLayout = findViewById(R.id.drawer);
        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
                refreshDrawerToggle();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
                refreshDrawerToggle();
            }
        });
        //actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        grpLogout = navigationView.findViewById(R.id.grp_logout);
        imgProfilePic = navigationView.getHeaderView(0).findViewById(R.id.img_profile_pic);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_email);

        /*getSupportActionBar().setTitle("DMAC");*/

        initializeUi();

        setOnClickListeners();

    }

    private void refreshDrawerToggle() {

        if(isDrawerOpen){
            imgToggle.setImageResource(R.drawable.ic_back);
        }
        else {
            imgToggle.setImageResource(R.drawable.ic_hamburger);
        }

    }

    private void initializeUi() {
        grpNewJob = findViewById(R.id.grp_new_job);

        sp = getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        UserData userData = gson.fromJson(sp.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);

        if(userData!= null){
            String name = (null != userData.getFullName()) ? userData.getFullName() : "NA";
            String email = (null != userData.getEmail()) ? userData.getEmail() : "NA";
            tvName.setText(name);
            tvEmail.setText(email);
        }
        else {
            tvName.setText("NA");
            tvEmail.setText("NA");
        }

    }


    private void setOnClickListeners() {

        grpNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, NewJobActivity.class));
            }
        });


        grpNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, NotificationActivity.class));
            }
        });

        grpNavToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrawerOpen){
                    drawerLayout.closeDrawer(Gravity.START);
                    //refreshDrawerToggle();
                    //isDrawerOpen = false;
                }
                else {
                    drawerLayout.openDrawer(Gravity.START);
                    //refreshDrawerToggle();
                }
            }
        });

        grpLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.logoutUser(CustomerHomeActivity.this);
            }
        });
    }


    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.navigation_change_company:
                // Do nothing:
                break;

            case R.id.navigation_notifications:
                startActivity(new Intent(CustomerHomeActivity.this, NotificationActivity.class));
                break;
        }

        drawerLayout.closeDrawer(Gravity.START);

        return true;
    }
}
