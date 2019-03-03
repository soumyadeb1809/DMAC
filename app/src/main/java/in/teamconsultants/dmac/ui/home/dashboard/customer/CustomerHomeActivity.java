package in.teamconsultants.dmac.ui.home.dashboard.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import in.teamconsultants.dmac.model.CustomerAccount;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.network.api.ApiClient;
import in.teamconsultants.dmac.network.api.ApiInterface;
import in.teamconsultants.dmac.network.dto.CustomerAccountsResponse;
import in.teamconsultants.dmac.ui.home.about.AboutUsActivity;
import in.teamconsultants.dmac.ui.home.accounts.ChangeAccountActivity;
import in.teamconsultants.dmac.ui.home.information.InformationFragment;
import in.teamconsultants.dmac.ui.home.jobs.CustomerJobsFragment;
import in.teamconsultants.dmac.ui.home.jobs.NewJobActivity;
import in.teamconsultants.dmac.ui.home.profile.ProfileFragment;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.reports.ReportsFragment;
import in.teamconsultants.dmac.ui.notification.NotificationActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.PermissionUtils;
import in.teamconsultants.dmac.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private LinearLayout grpNewJob;
    private LinearLayout grpNavToggle;
    private ImageView imgToggle;
    private LinearLayout grpLogout;

    private CircleImageView imgProfile;
    private TextView tvName, tvEmail;

    private boolean isDrawerOpen = false;

    private ApiInterface apiInterface;

    private ProgressDialog progress;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private String token;
    private UserData userData;

    private Fragment selectedFragment = null;

    private static final int PROFILE_PIC_REQ = 111;


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
                        selectedFragment = customerDashboardFragment;
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
                        selectedFragment = customerDashboardFragment;
                        return true;

                    case R.id.navigation_reports:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reportsFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        toolbarTitle.setText("Reports");
                        selectedFragment = customerDashboardFragment;
                        return true;

                    case R.id.navigation_info:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, informationFragment).commit();
                        grpNewJob.setVisibility(View.GONE);
                        toolbarTitle.setText("Information");
                        selectedFragment = customerDashboardFragment;
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

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Checkout.preload(getApplicationContext());

        gson = new Gson();
        sharedPreferences = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        token = sharedPreferences.getString(AppConstants.SP.TAG_TOKEN, "");
        progress = new ProgressDialog(this);

        navigation.setSelectedItemId(R.id.navigation_dashboard);


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

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        grpLogout = navigationView.findViewById(R.id.grp_logout);
        imgProfile = navigationView.getHeaderView(0).findViewById(R.id.img_profile_pic);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.txt_email);

        /*getSupportActionBar().setTitle("DMAC");*/

        initializeUi();

        setOnClickListeners();
        fetchCustomerAccounts();

    }

    @Override
    protected void onResume() {
        super.onResume();

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

        userData = gson.fromJson(sp.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);

        if(userData!= null){
            String name = (null != userData.getFullName()) ? userData.getFullName() : "NA";
            String email = (null != userData.getEmail()) ? userData.getEmail() : "NA";
            tvName.setText(name);
            tvEmail.setText(email);
            loadProfilePic();
        }
        else {
            tvName.setText("NA");
            tvEmail.setText("NA");
            Picasso.get().load(R.drawable.ic_user).into(imgProfile);
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
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    //refreshDrawerToggle();
                    //isDrawerOpen = false;
                }
                else {
                    drawerLayout.openDrawer(Gravity.LEFT);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.navigation_change_company:
                startActivity(new Intent(CustomerHomeActivity.this, ChangeAccountActivity.class));
                break;

            case R.id.navigation_notifications:
                startActivity(new Intent(CustomerHomeActivity.this, NotificationActivity.class));
                break;

            case R.id.navigation_about_us:
                startActivity(new Intent(CustomerHomeActivity.this, AboutUsActivity.class));
                break;
        }

        drawerLayout.closeDrawer(Gravity.LEFT);

        return true;
    }

    private void fetchCustomerAccounts(){

        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();

        final Call<CustomerAccountsResponse> customerAccountsCall = apiInterface.doGetCustomerAccountList(token);

        customerAccountsCall.enqueue(new Callback<CustomerAccountsResponse>() {
            @Override
            public void onResponse(Call<CustomerAccountsResponse> call, Response<CustomerAccountsResponse> response) {
                progress.dismiss();
                CustomerAccountsResponse customerAccountsResponse = response.body();

                if(customerAccountsResponse.getStatus().equals(AppConstants.RESPONSE.SUCCESS)){

                    List<CustomerAccount> customerAccountList = customerAccountsResponse.getAccountList();
                    Menu navDrawerMenu = navigationView.getMenu();

                    if(null == customerAccountList || customerAccountList.size() == 0){
                        navDrawerMenu.findItem(R.id.navigation_change_company).setVisible(false);
                    }
                    else {
                        navDrawerMenu.findItem(R.id.navigation_change_company).setVisible(true);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(AppConstants.SP.TAG_USER_ACCOUNT_LIST, gson.toJson(customerAccountsResponse));
                        editor.commit();

                    }

                    showSelectedFragment();

                }
                else {
                    Log.d(AppConstants.LOG_TAG, "Token expired\nresponse: " + gson.toJson(customerAccountsResponse));
                    Utility.forceLogoutUser(CustomerHomeActivity.this);
                }

            }

            @Override
            public void onFailure(Call<CustomerAccountsResponse> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Utility.showAlert(CustomerHomeActivity.this, "Info", "Something went wrong, please try again");
            }
        });



    }

    private void showSelectedFragment(){

        if(selectedFragment == null) {
            navigation.setSelectedItemId(R.id.navigation_dashboard);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, customerDashboardFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
        }
    }

    private void loadProfilePic(){

        String profilePicUrl = userData.getProfilePic();

        if(null == profilePicUrl || TextUtils.isEmpty(profilePicUrl.trim())){
            Picasso.get().load(R.drawable.ic_user).into(imgProfile);
        }
        else {
            Picasso.get().load(AppConstants.FILE_BASE_URL + profilePicUrl)
                    .placeholder(R.drawable.ic_user).into(imgProfile);
        }

    }

    @Override
    public void askForPermission(Fragment fragment) {

        PermissionUtils.askForStoragePermissions(this, PROFILE_PIC_REQ);

    }

    @Override
    public void updateNavigationProfileImg(String filePath) {
        if(null == filePath || TextUtils.isEmpty(filePath.trim())){
            Picasso.get().load(R.drawable.ic_user).into(imgProfile);
        }
        else {
            Picasso.get().load(AppConstants.FILE_BASE_URL + filePath)
                    .placeholder(R.drawable.ic_user).into(imgProfile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PROFILE_PIC_REQ){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();

            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
            }
        }

    }
}
