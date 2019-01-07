package in.teamconsultants.dmac.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.dashboard.customer.CustomerHomeActivity;
import in.teamconsultants.dmac.ui.registration.QuickRegisterFragment;
import in.teamconsultants.dmac.ui.registration.RegisterActivity;
import in.teamconsultants.dmac.utils.AppConstants;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText etUsername, etPassword;
    private LinearLayout btnCreateAccount, btnLogin;
    private CheckBox cbRememberMe;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = v.findViewById(R.id.et_username);
        etPassword = v.findViewById(R.id.et_password);
        btnLogin = v.findViewById(R.id.grp_login);
        btnCreateAccount = v.findViewById(R.id.grp_create_account);
        cbRememberMe = v.findViewById(R.id.cb_remember_me);

        setOnClickListeners();
        return v;
    }

    private void setOnClickListeners() {

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationAlert();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerHomeActivity.class));
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showRegistrationAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_create_account, null);
        builder.setView(dialogLayout);

        final AlertDialog dialog = builder.create();

        LinearLayout grpQuickReg = dialogLayout.findViewById(R.id.grp_quick_registration);
        LinearLayout grpRegularReg = dialogLayout.findViewById(R.id.grp_regular_registration);

        grpQuickReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.QUICK);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        grpRegularReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(AppConstants.INTENT_TAG.REG_TYPE, AppConstants.REGISTRATION.REGULAR);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
