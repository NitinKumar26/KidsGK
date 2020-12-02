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
import com.gadgetsfolk.kidsgk.model.QuizType
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import java.lang.String

class QuizTypeAdapter(private val mContext: Context, private var mQuizTypeList: MutableList<Any?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val tvImage: TextView = itemView.findViewById(R.id.tv_img)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.img)

    }

    override fun getItemCount(): Int {
        return mQuizTypeList.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem = mQuizTypeList[position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView = LayoutInflater.from(mContext).inflate(R.layout.ad_unified_new, parent, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.quiz_category_item, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.quiz_category_item, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = mQuizTypeList[position] as UnifiedNativeAd
                populateAdView(nativeAd, (holder as UnifiedNativeAdViewHolder).adView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val viewHolder = holder as ViewHolder
                val quizType = mQuizTypeList[position] as QuizType
                viewHolder.tvTitle.text = quizType.title
                viewHolder.tvSubTitle.text = quizType.sub_title
                viewHolder.tvImage.text = quizType.title?.get(0)?.toString()
                if (position % 5 == 0) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_two, null)
                else if (position % 5 == 1) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_three, null)
                else if (position % 5 == 2) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_four, null)
                else if (position % 5 == 3) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_five, null)
                else viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient, null)
            }
            else -> {
                val viewHolder = holder as ViewHolder
                val quizType = mQuizTypeList[position] as QuizType
                viewHolder.tvTitle.text = quizType.title
                viewHolder.tvSubTitle.text = quizType.sub_title
                viewHolder.tvImage.text = quizType.title?.get(0)?.toString()
                if (position % 5 == 0) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_two, null)
                else if (position % 5 == 1) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_three, null)
                else if (position % 5 == 2) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_four, null)
                else if (position % 5 == 3) viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_five, null)
                else viewHolder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient, null)
            }
        }
    }

    fun setItems(mQuizTypeList: MutableList<Any?>) {
        this.mQuizTypeList = mQuizTypeList
    }

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