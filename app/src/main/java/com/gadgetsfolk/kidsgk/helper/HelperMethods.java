package com.gadgetsfolk.kidsgk.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HelperMethods {


    public static int pix(Context activity, int dp){
        DisplayMetrics metrics = new DisplayMetrics();
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logincalDensity = metrics.density;
        return (int) Math.ceil(dp * logincalDensity);
    }

    public interface ClickListener {
        void onClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final HelperMethods.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
            if (child!=null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)){
                clickListener.onClick(recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }
}
