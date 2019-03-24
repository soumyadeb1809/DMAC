package in.teamconsultants.dmac.ui.home.information;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.ui.home.faq.FAQActivity;
import in.teamconsultants.dmac.ui.home.invoices.InvoicesActivity;
import in.teamconsultants.dmac.utils.AppConstants;


public class InformationFragment extends Fragment {

    private View v;

    private LinearLayout grpSubscriptions, grpFAQ, grpDMACSupport, grpRateUs;

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
        grpFAQ = v.findViewById(R.id.grp_faq);
        grpDMACSupport = v.findViewById(R.id.grp_dmac_support);
        grpRateUs = v.findViewById(R.id.grp_rate_us);

        grpSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InvoicesActivity.class));
            }
        });

        grpFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQActivity.class));
            }
        });

        grpDMACSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        grpRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPayStore();
            }
        });

    }


    private void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",AppConstants.INFO.DMAC_SUPPORT_EMAIL, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.INFO.DMAC_SUPPORT_SUB);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(Intent.createChooser(emailIntent, "DMAC Support"));
    }

    private void sendUserToPayStore(){
        Uri uri = Uri.parse(AppConstants.APP_MARKET_BASE_URL + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(AppConstants.PLAY_STORE_BASE_URL + getContext().getPackageName())));
        }
    }

}
