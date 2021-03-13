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
import com.gadgetsfolk.kidsgk.model.QuizType

class QuizTypeAdapter(private val mContext: Context, private var mQuizTypeList: MutableList<QuizType>) : RecyclerView.Adapter<QuizTypeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvSubTitle: TextView = itemView.findViewById(R.id.tv_subtitle)
        val tvImage: TextView = itemView.findViewById(R.id.tv_img)
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.img)

    }

    override fun getItemCount(): Int {
        return mQuizTypeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.quiz_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quizType = mQuizTypeList[position]
        holder.tvTitle.text = quizType.title
        holder.tvSubTitle.text = quizType.sub_title
        holder.tvImage.text = quizType.title?.get(0)?.toString()
        when {
            position % 5 == 0 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_two, null)
            position % 5 == 1 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_three, null)
            position % 5 == 2 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_four, null)
            position % 5 == 3 -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient_five, null)
            else -> holder.relativeLayout.background = ResourcesCompat.getDrawable(mContext.resources, R.drawable.gradient, null)
        }
    }

}