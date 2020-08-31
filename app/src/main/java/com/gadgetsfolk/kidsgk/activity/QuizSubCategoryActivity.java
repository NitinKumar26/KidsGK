package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.QuizType;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizSubCategoryActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;

    private FirebaseFirestore db;
    private QuizTypeAdapter quizTypeAdapter;
    private QuizType quizType;
    private InterstitialAd interstitialAd;

    //The AdLoader used to load ads
    private AdLoader adLoader;

    //List of videoItems and native ads that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    //List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sub_category);

        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();

        quizType = getIntent().getParcelableExtra("quiz_type");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        if (actionBar != null) if (quizType != null) actionBar.setTitle(quizType.getTitle());

        quizTypeAdapter = new QuizTypeAdapter(QuizSubCategoryActivity.this, mRecyclerViewItems);

        recyclerView.setLayoutManager(new LinearLayoutManager(QuizSubCategoryActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(quizTypeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(QuizSubCategoryActivity.this, RecyclerView.VERTICAL));

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(QuizSubCategoryActivity.this, position -> {
            if (quizTypeAdapter.getItemViewType(position) == 0) {
                QuizType quizSubType = (QuizType) mRecyclerViewItems.get(position);
                Intent intent = new Intent(QuizSubCategoryActivity.this, QuizActivity.class);
                intent.putExtra("quiz_type", quizType);
                intent.putExtra("quiz_sub_type", quizSubType);
                startActivity(intent);
            }
        }));

        getQuizSubCategories();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(QuizSubCategoryActivity.this.getResources().getString(R.string.interstitial_ad_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void getQuizSubCategories(){
        db.collection("quiz_type")
                .document(quizType.getDoc_id())
                .collection("quiz_sub_type").orderBy("title")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuizType> types = queryDocumentSnapshots.toObjects(QuizType.class);
                    mRecyclerViewItems.addAll(types);
                    //Collections.shuffle(mQuizSubTypeList);
                    quizTypeAdapter.setItems(mRecyclerViewItems);
                    quizTypeAdapter.notifyDataSetChanged();
                    loadNativeAds();
                }).addOnFailureListener(e -> Toast.makeText(QuizSubCategoryActivity.this, "Unable to get quizzes", Toast.LENGTH_LONG).show()).addOnCompleteListener(task -> progressBar.setVisibility(View.GONE));
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
            quizTypeAdapter.setItems(mRecyclerViewItems);
            quizTypeAdapter.notifyDataSetChanged();
        }
    }

    private void loadNativeAds() {
            AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit));
            adLoader = builder.forUnifiedNativeAd(
                    unifiedNativeAd -> {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            insertAdsInMenuItems(mNativeAds, mRecyclerViewItems);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

}
