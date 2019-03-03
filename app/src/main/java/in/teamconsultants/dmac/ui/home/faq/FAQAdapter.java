package in.teamconsultants.dmac.ui.home.faq;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.teamconsultants.dmac.R;
import in.teamconsultants.dmac.model.FAQ;
import in.teamconsultants.dmac.utils.AppConstants;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private List<FAQ> faqList;
    private Activity activity;

    public class FAQViewHolder extends RecyclerView.ViewHolder{

        public TextView tvQuestion, tvAnswer;
        public LinearLayout grpExpand, grpContent;
        public ImageView imgToggle;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.txt_question);
            tvAnswer = itemView.findViewById(R.id.txt_answer);
            grpExpand = itemView.findViewById(R.id.grp_expand);
            grpContent = itemView.findViewById(R.id.grp_content);
            imgToggle = itemView.findViewById(R.id.img_toggle);

        }
    }

    public FAQAdapter(Activity activity, List<FAQ> faqList) {
        this.faqList = faqList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_faq, viewGroup,false);
        return new FAQViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FAQViewHolder viewHolder, final int position) {

        final FAQ faq = faqList.get(position);

        if(faq != null){
            viewHolder.tvQuestion.setText(faq.getQuestion());
            viewHolder.tvAnswer.setText(faq.getAnswer());

            if(faq.isExpanded()){
                viewHolder.tvAnswer.setVisibility(View.VISIBLE);
                viewHolder.imgToggle.setImageResource(R.drawable.ic_arrow_up);
            }
            else {
                viewHolder.tvAnswer.setVisibility(View.GONE);
                viewHolder.imgToggle.setImageResource(R.drawable.ic_arrow_down);
            }


            viewHolder.grpExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(faq.isExpanded()){
                        viewHolder.tvAnswer.setVisibility(View.GONE);
                        faq.setExpanded(false);
                        viewHolder.imgToggle.setImageResource(R.drawable.ic_arrow_down);
                    }
                    else {
                        viewHolder.tvAnswer.setVisibility(View.VISIBLE);
                        faq.setExpanded(true);
                        viewHolder.imgToggle.setImageResource(R.drawable.ic_arrow_up);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }




}
