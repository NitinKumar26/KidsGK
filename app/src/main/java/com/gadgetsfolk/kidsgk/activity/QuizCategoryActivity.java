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
import com.gadgetsfolk.kidsgk.model.Quiz;
import com.gadgetsfolk.kidsgk.model.QuizType;
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

public class QuizCategoryActivity extends AppCompatActivity {
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;
    private ArrayList<QuizType> mQuizType;
    private FirebaseFirestore db;
    private QuizTypeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mQuizType = new ArrayList<>();

        adapter = new QuizTypeAdapter(QuizCategoryActivity.this, mQuizType);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuizCategoryActivity.this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(QuizCategoryActivity.this, RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(QuizCategoryActivity.this, position -> {
            Intent intent = new Intent(QuizCategoryActivity.this, QuizSubCategoryActivity.class);
            intent.putExtra("quiz_type", mQuizType.get(position));
            startActivity(intent);
        }));

        getQuizCategories();

    }


    private void getQuizCategories(){
        db.collection("quiz_type").get()
          .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<QuizType> types = queryDocumentSnapshots.toObjects(QuizType.class);
                mQuizType.addAll(types);
                Collections.shuffle(mQuizType);
                adapter.setItems(mQuizType);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizCategoryActivity.this, "Unable to get quizzes", Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
