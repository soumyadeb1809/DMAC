package in.teamconsultants.dmac.ui.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.teamconsultants.dmac.R;


public class InformationFragment extends Fragment {

    private View v;

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

        return v;
    }

}
