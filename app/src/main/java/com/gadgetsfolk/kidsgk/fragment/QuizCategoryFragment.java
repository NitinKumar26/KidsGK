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
import com.gadgetsfolk.kidsgk.activity.QuizSubCategoryActivity;
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.QuizType;
//import com.google.android.ads.mediationtestsuite.MediationTestSuite;
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

public class QuizCategoryFragment extends Fragment {
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    private QuizTypeAdapter adapter;

    //The AdLoader used to load ads
    private AdLoader adLoader;

    //List of quizItems and native ads that populate the RecyclerView;
    private List<Object> mRecyclerViewItems = new ArrayList<>();
    //List of nativeAds that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        adapter = new QuizTypeAdapter(getContext(), mRecyclerViewItems);
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        }
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(getContext(), position -> {
            if (adapter.getItemViewType(position) == 0){
                QuizType quizType = (QuizType) mRecyclerViewItems.get(position);
                Intent intent = new Intent(getContext(), QuizSubCategoryActivity.class);
                intent.putExtra("quiz_type", quizType);
                startActivity(intent);
            }
        }));

        getQuizCategories();

        //MediationTestSuite.launch(getContext());

    }

    private void getQuizCategories(){
        db.collection("quiz_type").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizType> types = queryDocumentSnapshots.toObjects(QuizType.class);
                    mRecyclerViewItems.addAll(types);
                    Collections.shuffle(mRecyclerViewItems);
                    adapter.setItems(mRecyclerViewItems);
                    adapter.notifyDataSetChanged();
                    loadNativeAds();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Unable to get quizzes", Toast.LENGTH_LONG).show())
                .addOnCompleteListener(task ->
                        progressBar.setVisibility(View.GONE));
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
            adapter.setItems(mRecyclerViewItems);
            adapter.notifyDataSetChanged();
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
                                //adapter.notifyDataSetChanged();
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
                                //adapter.notifyDataSetChanged();
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
