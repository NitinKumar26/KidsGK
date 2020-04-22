package com.gadgetsfolk.kidsgk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.model.QuizType;
import java.util.ArrayList;

public class QuizTypeAdapter extends RecyclerView.Adapter<QuizTypeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<QuizType> mQuizTypeList;

    public QuizTypeAdapter(Context mContext, ArrayList<QuizType> mQuizTypeList) {
        this.mContext = mContext;
        this.mQuizTypeList = mQuizTypeList;
    }

    @NonNull
    @Override
    public QuizTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.quiz_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizTypeAdapter.ViewHolder holder, int position) {
        QuizType quizType = mQuizTypeList.get(position);
        holder.tvTitle.setText(quizType.getTitle());
        holder.tvSubTitle.setText(quizType.getSub_title());
        holder.tvImage.setText(String.valueOf(quizType.getTitle().charAt(0)));

        if (position % 5 == 0) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_two));
        else if (position % 5 == 1) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_three));
        else if (position % 5 == 2) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_four));
        else if (position % 5 == 3) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_five));
        else holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient));
    }


    @Override
    public int getItemCount() {
        return mQuizTypeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvSubTitle, tvImage;
        private RelativeLayout relativeLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubTitle = itemView.findViewById(R.id.tv_subtitle);
            relativeLayout = itemView.findViewById(R.id.img);
            tvImage = itemView.findViewById(R.id.tv_img);
        }
    }

    public void setItems(ArrayList<QuizType> mQuizTypeList){
        this.mQuizTypeList = mQuizTypeList;
    }
}
