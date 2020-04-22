package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.gadgetsfolk.kidsgk.adapter.VideoAdapter;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.gadgetsfolk.kidsgk.model.Video;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress_circular)
    ProgressBar progressBar;

    private VideoAdapter videoAdapter;
    private ArrayList<Video> mVideoList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mVideoList = new ArrayList<>();

        videoAdapter = new VideoAdapter(this, mVideoList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(VideoActivity.this, RecyclerView.VERTICAL));
        recyclerView.setAdapter(videoAdapter);

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(VideoActivity.this, position -> {
            Intent intent = new Intent(VideoActivity.this, PlayerActivity.class);
            intent.putExtra("videoID", mVideoList.get(position).getVideo_link());
            startActivity(intent);
        }));

        getVideos();
    }

    private void getVideos(){
        db.collection("videos").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Video> types = queryDocumentSnapshots.toObjects(Video.class);
                    mVideoList.addAll(types);
                    Collections.shuffle(mVideoList);
                    videoAdapter.setItems(mVideoList);
                    videoAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e -> Toast.makeText(VideoActivity.this, "Unable to get quizzes", Toast.LENGTH_LONG).show())
                .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE));
    }

}
