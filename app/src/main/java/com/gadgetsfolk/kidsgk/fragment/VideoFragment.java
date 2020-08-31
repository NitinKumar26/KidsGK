package com.gadgetsfolk.kidsgk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.activity.PlayerActivity;
import com.gadgetsfolk.kidsgk.adapter.VideoAdapter;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.Video;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    //private ArrayList<Video> mVideoList;
    private FirebaseFirestore db;

    //The AdLoader used to load ads
    private AdLoader adLoader;

    //List of videoItems and native ads that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    //List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, view);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        //mVideoList = new ArrayList<>();

        videoAdapter = new VideoAdapter(getContext(),  mRecyclerViewItems);
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        }
        recyclerView.setAdapter(videoAdapter);

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(getContext(), position -> {
            if (videoAdapter.getItemViewType(position) == 0){
                Video video = (Video) mRecyclerViewItems.get(position);
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("videoID", video.getVideo_link());
                startActivity(intent);
            }
        }));

        getVideos();

    }

    private void getVideos(){
        db.collection("videos").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> types = queryDocumentSnapshots.toObjects(Video.class);
                    mRecyclerViewItems.addAll(types);
                    Collections.shuffle(mRecyclerViewItems);
                    videoAdapter.setItems(mRecyclerViewItems);
                    videoAdapter.notifyDataSetChanged();
                    loadNativeAds();
                }).addOnFailureListener(e -> Toast.makeText(getContext(), "Unable to get quizzes", Toast.LENGTH_LONG).show())
                .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE));
    }

    private void insertAdsInMenuItems(List<UnifiedNativeAd> mNativeAds, List<Object> mRecyclerViewItems) {
        if (mNativeAds.size() <= 0) {
            return;
        }

        int offset = (mRecyclerViewItems.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index = index + offset;
            videoAdapter.setItems(mRecyclerViewItems);
            videoAdapter.notifyDataSetChanged();
        }
    }

    private void loadNativeAds() {
        if (getContext() != null) {
            AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.native_ad_unit));
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // A native ad loaded successfully, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            mNativeAds.add(unifiedNativeAd);
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems);
                            }
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.");
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems);
                            }
                        }

                        @Override
                        public void onAdClicked() {
                            //super.onAdClicked();
                            //Ad Clicked
                            Log.e("adclicked", "yes");
                        }
                    }).build();

            //Number of Native Ads to load
            int NUMBER_OF_ADS;
            if (mRecyclerViewItems.size() <= 9)
                NUMBER_OF_ADS = 3;
            else {
                NUMBER_OF_ADS = (mRecyclerViewItems.size() / 5) + 1;
            }

            // Load the Native ads.
            adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);

            Log.e("numberOfAds", String.valueOf(NUMBER_OF_ADS));
        }
    }
}
