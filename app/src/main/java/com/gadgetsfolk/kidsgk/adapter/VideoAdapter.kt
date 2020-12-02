package com.gadgetsfolk.kidsgk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.helper.HelperMethods.populateAdView
import com.gadgetsfolk.kidsgk.model.Video
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

class VideoAdapter(private val mContext: Context, private var mVideoList: MutableList<Any?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val tvImage: TextView = itemView.findViewById(R.id.tv_img)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.img)

    }

    override fun getItemCount(): Int {
        return mVideoList.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem = mVideoList[position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView: View = LayoutInflater.from(mContext).inflate(R.layout.ad_unified_new, parent, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = mVideoList[position] as UnifiedNativeAd
                populateAdView(nativeAd, (holder as UnifiedNativeAdViewHolder).adView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val viewHolder = holder as ViewHolder
                val video = mVideoList[position] as Video
                viewHolder.tvTitle.text = video.title
                viewHolder.tvSubTitle.text = video.sub_title
                viewHolder.tvImage.text = video.title?.get(0)?.toString()
                when {
                    position % 5 == 0 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_two, null)
                    position % 5 == 1 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_three, null)
                    position % 5 == 2 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_four, null)
                    position % 5 == 3 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_five, null)
                    else -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient, null)
                }
            }
            else -> {
                val viewHolder = holder as ViewHolder
                val video = mVideoList[position] as Video
                viewHolder.tvTitle.text = video.title
                viewHolder.tvSubTitle.text = video.sub_title
                viewHolder.tvImage.text = video.title?.get(0)?.toString()
                when {
                    position % 5 == 0 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_two, null)
                    position % 5 == 1 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_three, null)
                    position % 5 == 2 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_four, null)
                    position % 5 == 3 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_five, null)
                    else -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient, null)
                }
            }
        }
    }

    fun setItems(mQuizTypeList: MutableList<Any?>) {
        mVideoList = mQuizTypeList
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

        / *
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

        / *

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

    companion object {
        //A Menu ItemView type
        private const val MENU_ITEM_VIEW_TYPE = 0
        private const val UNIFIED_NATIVE_AD_VIEW_TYPE = 1
    }

    internal class UnifiedNativeAdViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val adView: UnifiedNativeAdView = view.findViewById(R.id.unified_ad)

        init {
            adView.mediaView = view.findViewById(R.id.ad_media) as MediaView
            adView.headlineView = view.findViewById(R.id.ad_headline)
            adView.iconView = view.findViewById(R.id.ad_icon)
            adView.priceView = view.findViewById(R.id.ad_price)
            adView.starRatingView = view.findViewById(R.id.ad_stars)
        }
    }


}