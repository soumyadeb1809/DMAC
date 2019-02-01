package in.teamconsultants.dmac.ui.registration;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.utils.AppConstants;

public class RegisterActivity extends AppCompatActivity implements QuickRegisterFragment.OnQuickRegisterFragmentInteractionListener{

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        int registrationType = intent.getIntExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.QUICK);
        int initiator = intent.getIntExtra(AppConstants.INTENT_TAG.REG_INITIATOR, AppConstants.REGISTRATION.INITIATOR.CUSTOMER);

        if(registrationType == AppConstants.REGISTRATION.REGULAR) {
            /*getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegularRegisterFragment()).commit();
            if(initiator == AppConstants.REGISTRATION.INITIATOR.CUSTOMER) {
                getSupportActionBar().setTitle("Regular Registration");
            }
            else {
                getSupportActionBar().setTitle("Add New Account");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }*/
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new QuickRegisterFragment()).commit();
            getSupportActionBar().setTitle("Quick Registration");
        }

    }

    @Override
    public void onQuickRegisterFragmentInteraction(Uri uri) {

    }

}
