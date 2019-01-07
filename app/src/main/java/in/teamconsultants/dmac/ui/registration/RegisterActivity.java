package in.teamconsultants.dmac.ui.registration;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.utils.AppConstants;

public class RegisterActivity extends AppCompatActivity implements QuickRegisterFragment.OnQuickRegisterFragmentInteractionListener,
        RegularRegisterFragment.OnRegularRegisterFragmentInteractionListener{

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        int registrationType = intent.getIntExtra("reg_type", AppConstants.REGISTRATION.QUICK);

        if(registrationType == AppConstants.REGISTRATION.REGULAR) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegularRegisterFragment()).commit();
            getSupportActionBar().setTitle("Regular Registration");
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new QuickRegisterFragment()).commit();
            getSupportActionBar().setTitle("Quick Registration");
        }

    }

    @Override
    public void onQuickRegisterFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRegularRegisterFragmentInteraction(Uri uri) {

    }
}
