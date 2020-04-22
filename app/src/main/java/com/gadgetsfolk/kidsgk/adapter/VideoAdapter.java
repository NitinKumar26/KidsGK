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
import com.gadgetsfolk.kidsgk.model.Video;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Video> mVideoList;

    public VideoAdapter(Context mContext, ArrayList<Video> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.tvTitle.setText(video.getTitle());
        holder.tvSubTitle.setText(video.getSub_title());
        holder.tvImage.setText(String.valueOf(video.getTitle().charAt(0)));

        if (position % 5 == 0) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_two));
        else if (position % 5 == 1) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_three));
        else if (position % 5 == 2) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_four));
        else if (position % 5 == 3) holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_five));
        else holder.relativeLayout.setBackground(mContext.getResources().getDrawable(R.drawable.gradient));
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public void setItems(ArrayList<Video> mQuizTypeList){
        this.mVideoList = mQuizTypeList;
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
}
