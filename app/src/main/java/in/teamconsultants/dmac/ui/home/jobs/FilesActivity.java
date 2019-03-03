package in.teamconsultants.dmac.ui.home.jobs;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.utils.AppConstants;

public class FilesActivity extends AppCompatActivity implements CustomerJobsFragment.OnCustomerJobsFragmentInteractionListener {

    public static String categoryName;
    public static String categoryId;

    private CustomerJobsFragment jobsFragment;

    {
        jobsFragment = new CustomerJobsFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        Intent intent = getIntent();

        if(intent == null){
            finish();
        }

        else {
            categoryName = intent.getStringExtra(AppConstants.INTENT_TAG.FILE_CATEGORY_NAME);
            categoryId = intent.getStringExtra(AppConstants.INTENT_TAG.FILE_CATEGORY_ID);

            if(null == categoryId || null == categoryName){
                finish();
            }
            else if(TextUtils.isEmpty(categoryId) || TextUtils.isEmpty(categoryName)){
                finish();
            }
            else {
                getSupportFragmentManager().beginTransaction().replace(R.id.content, jobsFragment).commit();
            }
        }

    }
}
