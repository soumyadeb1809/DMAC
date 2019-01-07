package in.teamconsultants.dmac.ui.login;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.teamconsultants.dmac.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
