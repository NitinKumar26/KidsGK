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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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

    fun setItems(mQuizTypeList: MutableList<Any?>) {
        mVideoList = mQuizTypeList
    }
}