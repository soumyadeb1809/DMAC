package in.teamconsultants.dmac.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.google.gson.Gson;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerHomeActivity;
import in.teamconsultants.dmac.ui.home.dashboard.fe.FeHomeActivity;
import in.teamconsultants.dmac.ui.login.LoginActivity;
import in.teamconsultants.dmac.utils.AppConstants;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 1500;
    private SharedPreferences spUser;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        spUser = getSharedPreferences(AppConstants.SP.SP_USER_DATA, MODE_PRIVATE);
        startSplash();

    }



    private void startSplash() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                validateUser();


            }
        }, SPLASH_TIME_OUT);
    }

    private void validateUser() {
        boolean isUserLoggedIn = spUser.getBoolean(AppConstants.SP.IS_USER_LOGGED_IN, false);

        if(isUserLoggedIn){
            String token = spUser.getString(AppConstants.SP.TAG_TOKEN, "");
            if(TextUtils.isEmpty(token)){
                Log.d(AppConstants.LOG_TAG, "empty token");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

            UserData userData = gson.fromJson(spUser.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);
            if(null == userData){
                Log.d(AppConstants.LOG_TAG, "empty userData");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
            else if(userData.getRoleId().equals(AppConstants.USER_ROLE.CUSTOMER)) {
                Log.d(AppConstants.LOG_TAG, "customer role");
                startActivity(new Intent(SplashActivity.this, CustomerHomeActivity.class));
                finish();
            }
            else if(userData.getRoleId().equals(AppConstants.USER_ROLE.FE)) {
                Log.d(AppConstants.LOG_TAG, "FE role");
                startActivity(new Intent(SplashActivity.this, FeHomeActivity.class));
                finish();
            }
            else{
                Log.d(AppConstants.LOG_TAG, "unknown role");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
        else {
            Log.d(AppConstants.LOG_TAG, "No logged in user");
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}
