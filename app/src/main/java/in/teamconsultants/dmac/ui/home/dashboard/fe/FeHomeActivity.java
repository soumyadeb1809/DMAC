package in.teamconsultants.dmac.ui.home.dashboard.fe;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.accounts.AccountsFragment;
import in.teamconsultants.dmac.ui.home.jobs.CustomerJobsFragment;
import in.teamconsultants.dmac.ui.home.profile.ProfileFragment;
import in.teamconsultants.dmac.ui.registration.RegisterActivity;
import in.teamconsultants.dmac.ui.registration.RegularRegistrationActivity;
import in.teamconsultants.dmac.utils.AppConstants;

public class FeHomeActivity extends AppCompatActivity implements FeDashboardFragment.OnFeDashboardInteractionListener,
AccountsFragment.OnAccountsInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener{

    private BottomNavigationView navigation;

    private FeDashboardFragment feDashboardFragment;
    private AccountsFragment accountsFragment;
    private ProfileFragment profileFragment;

    private LinearLayout grpNewAccount;

    {
        feDashboardFragment = new FeDashboardFragment();
        accountsFragment = new AccountsFragment();
        profileFragment = new ProfileFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() != navigation.getSelectedItemId()) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, feDashboardFragment).commit();
                        grpNewAccount.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.navigation_account_list:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountsFragment).commit();
                        grpNewAccount.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        grpNewAccount.setVisibility(View.GONE);
                        return true;
                }
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fe_home);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, feDashboardFragment).commit();

        initializeUi();

        setOnclickListeners();
    }

    private void setOnclickListeners() {
        grpNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeHomeActivity.this, RegularRegistrationActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.REGULAR);
                intent.putExtra(AppConstants.INTENT_TAG.REG_INITIATOR, AppConstants.REGISTRATION.INITIATOR.FE);
                startActivity(intent);
            }
        });
    }

    private void initializeUi() {
        grpNewAccount = findViewById(R.id.grp_new_account);
    }


    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }
}
