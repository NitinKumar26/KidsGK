package com.gadgetsfolk.kidsgk.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.gadgetsfolk.kidsgk.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_quiz)
    void startQuizActivity(){
        startActivity(new Intent(MainActivity.this, QuizCategoryActivity.class));
    }

    @OnClick(R.id.btn_video)
    void startVideoActivity(){
        startActivity(new Intent(MainActivity.this, VideoActivity.class));
    }
}
