package in.teamconsultants.dmac.ui.home.faq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FAQ;

public class FAQActivity extends AppCompatActivity {

    private LinearLayout grpBack;
    private RecyclerView rvFaq;
    private FAQAdapter faqAdapter;

    private List<FAQ> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initializeUi();

    }

    private void initializeUi() {
        grpBack = findViewById(R.id.grp_back);
        rvFaq = findViewById(R.id.rv_faq);

        grpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prepareList();

        faqAdapter = new FAQAdapter(this, faqList);
        rvFaq.setLayoutManager(new LinearLayoutManager(this));
        rvFaq.setAdapter(faqAdapter);

    }

    private void prepareList() {

        faqList = new ArrayList<>();

        FAQ faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_1));
        faq.setAnswer(getResources().getString(R.string.faq_a_1));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_2));
        faq.setAnswer(getResources().getString(R.string.faq_a_2));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_3));
        faq.setAnswer(getResources().getString(R.string.faq_a_3));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_4));
        faq.setAnswer(getResources().getString(R.string.faq_a_4));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_5));
        faq.setAnswer(getResources().getString(R.string.faq_a_5));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_6));
        faq.setAnswer(getResources().getString(R.string.faq_a_6));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_7));
        faq.setAnswer(getResources().getString(R.string.faq_a_7));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_8));
        faq.setAnswer(getResources().getString(R.string.faq_a_8));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_9));
        faq.setAnswer(getResources().getString(R.string.faq_a_9));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_10));
        faq.setAnswer(getResources().getString(R.string.faq_a_10));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_11));
        faq.setAnswer(getResources().getString(R.string.faq_a_11));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_12));
        faq.setAnswer(getResources().getString(R.string.faq_a_12));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_13));
        faq.setAnswer(getResources().getString(R.string.faq_a_13));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_14));
        faq.setAnswer(getResources().getString(R.string.faq_a_14));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_15));
        faq.setAnswer(getResources().getString(R.string.faq_a_15));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_16));
        faq.setAnswer(getResources().getString(R.string.faq_a_16));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_17));
        faq.setAnswer(getResources().getString(R.string.faq_a_17));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_18));
        faq.setAnswer(getResources().getString(R.string.faq_a_18));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_19));
        faq.setAnswer(getResources().getString(R.string.faq_a_19));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_20));
        faq.setAnswer(getResources().getString(R.string.faq_a_20));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_21));
        faq.setAnswer(getResources().getString(R.string.faq_a_21));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_22));
        faq.setAnswer(getResources().getString(R.string.faq_a_22));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_23));
        faq.setAnswer(getResources().getString(R.string.faq_a_23));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_24));
        faq.setAnswer(getResources().getString(R.string.faq_a_24));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_25));
        faq.setAnswer(getResources().getString(R.string.faq_a_25));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_26));
        faq.setAnswer(getResources().getString(R.string.faq_a_26));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_27));
        faq.setAnswer(getResources().getString(R.string.faq_a_27));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_28));
        faq.setAnswer(getResources().getString(R.string.faq_a_28));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_29));
        faq.setAnswer(getResources().getString(R.string.faq_a_29));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_30));
        faq.setAnswer(getResources().getString(R.string.faq_a_30));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_31));
        faq.setAnswer(getResources().getString(R.string.faq_a_31));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_32));
        faq.setAnswer(getResources().getString(R.string.faq_a_32));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_33));
        faq.setAnswer(getResources().getString(R.string.faq_a_33));
        faqList.add(faq);


        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_34));
        faq.setAnswer(getResources().getString(R.string.faq_a_34));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_35));
        faq.setAnswer(getResources().getString(R.string.faq_a_35));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_36));
        faq.setAnswer(getResources().getString(R.string.faq_a_36));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_37));
        faq.setAnswer(getResources().getString(R.string.faq_a_37));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_38));
        faq.setAnswer(getResources().getString(R.string.faq_a_38));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_39));
        faq.setAnswer(getResources().getString(R.string.faq_a_39));
        faqList.add(faq);

        faq = new FAQ();
        faq.setQuestion(getResources().getString(R.string.faq_q_41));
        faq.setAnswer(getResources().getString(R.string.faq_a_41));
        faqList.add(faq);

    }
}
