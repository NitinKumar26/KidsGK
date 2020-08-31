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
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.QuizType;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class QuizTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //A Menu ItemView type
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private Context mContext;
    private List<Object> mQuizTypeList;

    public QuizTypeAdapter(Context mContext, List<Object> mQuizTypeList) {
        this.mContext = mContext;
        this.mQuizTypeList = mQuizTypeList;
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

    @Override
    public int getItemCount() {
        return mQuizTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = mQuizTypeList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(mContext).inflate(R.layout.ad_unified_new, parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                //Fall Through
            default:
                View view = LayoutInflater.from(mContext).inflate(R.layout.quiz_category_item, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mQuizTypeList.get(position);
                HelperMethods.populateAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                //Fall Through
            default:
                ViewHolder viewHolder = (ViewHolder) holder;

                QuizType quizType = (QuizType) mQuizTypeList.get(position);
                viewHolder.tvTitle.setText(quizType.getTitle());
                viewHolder.tvSubTitle.setText(quizType.getSub_title());
                viewHolder.tvImage.setText(String.valueOf(quizType.getTitle().charAt(0)));

                if (position % 5 == 0) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_two));
                else if (position % 5 == 1) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_three));
                else if (position % 5 == 2) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_four));
                else if (position % 5 == 3) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_five));
                else viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient));
        }

    }

    public void setItems(List<Object> mQuizTypeList){
        this.mQuizTypeList = mQuizTypeList;
    }
}
