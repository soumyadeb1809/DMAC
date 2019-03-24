package in.teamconsultants.dmac.ui.registration;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.utils.AppConstants;

public class RegisterActivity extends AppCompatActivity implements QuickRegisterFragment.OnQuickRegisterFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
            //getSupportActionBar().setTitle("Quick Registration");
        }

    }

    @Override
    public void onQuickRegisterFragmentInteraction(Uri uri) {

    }

}
