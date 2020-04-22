package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
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

    private ArrayList<QuizType> mQuizSubTypeList;
    private FirebaseFirestore db;
    private QuizTypeAdapter quizTypeAdapter;
    private QuizType quizType;
    private InterstitialAd interstitialAd;

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

        mQuizSubTypeList = new ArrayList<>();
        //mQuizSubTypeList.add(new QuizType("Currency", "4 Types Quiz", R.color.colorGrey));
        //mQuizSubTypeList.add(new QuizType("Independence Date", "4 Types Quiz", R.color.colorGrey));
        quizTypeAdapter = new QuizTypeAdapter(QuizSubCategoryActivity.this, mQuizSubTypeList);

        recyclerView.setLayoutManager(new LinearLayoutManager(QuizSubCategoryActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(quizTypeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(QuizSubCategoryActivity.this, RecyclerView.VERTICAL));

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(QuizSubCategoryActivity.this, new HelperMethods.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(QuizSubCategoryActivity.this, QuizActivity.class);
                intent.putExtra("quiz_type", quizType);
                intent.putExtra("quiz_sub_type", mQuizSubTypeList.get(position));
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
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<QuizType> types = queryDocumentSnapshots.toObjects(QuizType.class);
                mQuizSubTypeList.addAll(types);
                //Collections.shuffle(mQuizSubTypeList);
                quizTypeAdapter.setItems(mQuizSubTypeList);
                quizTypeAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizSubCategoryActivity.this, "Unable to get quizzes", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }
}
