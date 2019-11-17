package com.gadgetsfolk.kidsgk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.adapter.QuestionFragmentPagerAdapter;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.Quiz;
import com.gadgetsfolk.kidsgk.model.QuizType;
import com.gadgetsfolk.kidsgk.model.Score;
import com.gadgetsfolk.kidsgk.utils.NonSwipeableViewpager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {
    public static NonSwipeableViewpager viewPager;
    @BindView(R.id.indicator)
    TabLayout tabLayout;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    public static ArrayList<Quiz> mQuizList;
    private FirebaseFirestore db;
    private QuizType mQuizType, mQuizSubType;
    public static Score score = new Score();
    public static int questionsAttempted = 0;
    public static int quizScore = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        score = new Score();
        questionsAttempted = 0;
        quizScore = 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = FirebaseFirestore.getInstance();

        ButterKnife.bind(this);

        mQuizType = getIntent().getParcelableExtra("quiz_type");
        mQuizSubType = getIntent().getParcelableExtra("quiz_sub_type");

        viewPager = findViewById(R.id.viewPager);

        mQuizList = new ArrayList<>();
        getQuestions();

        viewPager.setPageMargin(HelperMethods.pix(QuizActivity.this, 24));
        viewPager.setClipToPadding(false);
        viewPager.setPadding(HelperMethods.pix(QuizActivity.this, 20), HelperMethods.pix(QuizActivity.this, 0),
                HelperMethods.pix(QuizActivity.this, 20),HelperMethods.pix(QuizActivity.this, 20));


    }

    private void getQuestions(){
        db.collection("quiz_type")
                .document(mQuizType.getDoc_id())
                .collection("quiz_sub_type")
                .document(mQuizSubType.getDoc_id())
                .collection("questions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Quiz> questions = queryDocumentSnapshots.toObjects(Quiz.class);
                mQuizList.addAll(questions);
                Collections.shuffle(mQuizList);
                QuestionFragmentPagerAdapter questionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(questionFragmentPagerAdapter); //Set the DealSliderAdapter with Viewpager;
                viewPager.setOffscreenPageLimit(mQuizList.size() + 1);
                tabLayout.setupWithViewPager(viewPager);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizActivity.this, "Unable to get questions.", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
