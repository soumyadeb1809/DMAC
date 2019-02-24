package in.teamconsultants.dmac.ui.home.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.User;
import in.teamconsultants.dmac.model.UserData;
import in.teamconsultants.dmac.ui.login.LoginActivity;
import in.teamconsultants.dmac.utils.AppConstants;
import in.teamconsultants.dmac.utils.Utility;


public class ProfileFragment extends Fragment {

    private OnProfileFragmentInteractionListener mListener;

    private TextView tvName, tvEmail, tvPhone, tvRole, tvAccountName, tvKeyUser;

    private LinearLayout grpLogout;

    private SharedPreferences sp;

    public ProfileFragment() {
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeUi(v);

        populateData();

        return v;
    }



    private void initializeUi(View v) {

        tvName = v.findViewById(R.id.txt_name);
        tvEmail = v.findViewById(R.id.txt_email);
        tvPhone = v.findViewById(R.id.txt_phone);
        tvRole = v.findViewById(R.id.txt_role);
        tvAccountName = v.findViewById(R.id.txt_account_name);
        tvKeyUser = v.findViewById(R.id.txt_key_user);

       /* grpLogout = v.findViewById(R.id.grp_logout);*/

        setOnClickListeners();

    }


    private void populateData() {

        //User user = new User();

        sp = getContext().getSharedPreferences(AppConstants.SP.SP_USER_DATA, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        UserData userData = gson.fromJson(sp.getString(AppConstants.SP.TAG_USER_DETAILS, null), UserData.class);

       /* user.setName("FE DMAC");
        user.setAccountName("DMAC");
        user.setEmail("fe@dmac.com");
        user.setPhone("9876543210");
        user.setKeyUser(true);
        user.setRole("Field Executive");
        user.setCreatedAt("2018-11-26 01:38:14");
        user.setUpdatedAt("0000-00-00 00:00:00");*/

        tvName.setText(userData.getFullName());
        tvEmail.setText(userData.getEmail());
        if(TextUtils.isEmpty(userData.getPhone())){
            tvPhone.setText("NA");
        }
        else {
            tvPhone.setText(userData.getPhone());
        }

        String role = "NA";
        if(userData.getRoleId().equals(AppConstants.USER_ROLE.FE)){
            role = "Field Executive";
        }
        else {
            role = "Customer";
        }
        tvRole.setText(role);
        tvAccountName.setText(userData.getAccountId());
        if(userData.getKeyUser().equals("1")) {
            tvKeyUser.setText("Yes");
        }
        else {
            tvKeyUser.setText("No");
        }

    }

    private void setOnClickListeners() {

/*
        grpLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               */
/* SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();*//*

                Utility.logoutUser(getActivity());
            }
        });
*/

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentInteractionListener) {
            mListener = (OnProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnProfileFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(Uri uri);
    }
}
