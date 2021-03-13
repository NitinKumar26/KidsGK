package com.gadgetsfolk.kidsgk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.model.MoreAppsItem;

import java.util.ArrayList;

public class MoreAppsAdapter extends RecyclerView.Adapter<MoreAppsAdapter.ViewHolder> {
    private ArrayList<MoreAppsItem>  moreAppsItems;
    private final Context mContext;

    public MoreAppsAdapter(Context mContext, ArrayList<MoreAppsItem> moreAppsItems) {
        this.moreAppsItems = moreAppsItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.more_apps_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoreAppsItem item = moreAppsItems.get(position);

        Glide.with(mContext).load(item.getIcon_url()).into(holder.imgAppIcon);

        holder.tvAppName.setText(item.getApp_name());
        holder.tvAppDesc.setText(item.getApp_desc());
    }

    @Override
    public int getItemCount() {
        return moreAppsItems.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAppName, tvAppDesc;
        private final ImageView imgAppIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAppIcon = itemView.findViewById(R.id.img);
            tvAppName = itemView.findViewById(R.id.tv_title);
            tvAppDesc = itemView.findViewById(R.id.tv_subtitle);
        }
    }

    public void setItems(ArrayList<MoreAppsItem> items){
        this.moreAppsItems = items;
    }

}
