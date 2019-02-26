package in.teamconsultants.dmac.ui.home.information;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.invoices.InvoicesActivity;


public class InformationFragment extends Fragment {

    private View v;

    private LinearLayout grpSubscriptions, grpFAQ;

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_information, container, false);
        initializeUi();
        return v;
    }

    private void initializeUi() {

        grpSubscriptions = v.findViewById(R.id.grp_subscriptions);

        grpSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InvoicesActivity.class));
            }
        });

    }

}
