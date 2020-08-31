package com.gadgetsfolk.kidsgk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.Video;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //A Menu ItemView type
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private Context mContext;
    private List<Object> mVideoList;

    public VideoAdapter(Context mContext, List<Object> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        return mVideoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = mVideoList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(mContext).inflate(R.layout.ad_unified_new, parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                //Fall Through
            default:
                View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mVideoList.get(position);
                HelperMethods.populateAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                //Fall through
            default:
                ViewHolder viewHolder = (ViewHolder) holder;
                Video video = (Video) mVideoList.get(position);
                viewHolder.tvTitle.setText(video.getTitle());
                viewHolder.tvSubTitle.setText(video.getSub_title());
                viewHolder.tvImage.setText(String.valueOf(video.getTitle().charAt(0)));

                if (position % 5 == 0) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_two));
                else if (position % 5 == 1) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_three));
                else if (position % 5 == 2) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_four));
                else if (position % 5 == 3) viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_five));
                else viewHolder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient));
        }

    }

    public void setItems(List<Object> mQuizTypeList){
        this.mVideoList = mQuizTypeList;
    }

    /*

    private void populateAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView){
        //Some assets are guaranteed to be in every UnifiedNativeAd
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        //((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        //These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        //check before trying to display them
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null)
            adView.getIconView().setVisibility(View.INVISIBLE);
        else{
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null)
            adView.getPriceView().setVisibility(View.INVISIBLE);
        else{
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        /*
        if (nativeAd.getStore() == null)
            adView.getStoreView().setVisibility(View.INVISIBLE);
        else{
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }



        if (nativeAd.getStarRating() == null)
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        else{
            adView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
        }

        /*

        if (nativeAd.getAdvertiser() == null)
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        else{
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }

        //Assign native ad object to the native view
        adView.setNativeAd(nativeAd);

    }
     */


}
