package com.gadgetsfolk.kidsgk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.model.Score;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreActivity extends AppCompatActivity {
    @BindView(R.id.tv_correct)
    TextView tvCorrect;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_you_have)
    TextView tvYouHave;
    @BindView(R.id.tv_out_of)
    TextView tvOutOf;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_play_again)
    MaterialButton tvPlayAgain;

    private InterstitialAd mInterStitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ButterKnife.bind(this);

        Score score = getIntent().getParcelableExtra("score_obj");

        if (score != null){
         tvCorrect.setText(score.getCorrect_questions());
         tvTotal.setText(score.getTotal_questions());
         tvYouHave.setText(getString(R.string.tv_correct, score.getCorrect_questions()));
         tvOutOf.setText(getString(R.string.tv_out_of, score.getTotal_questions()));
         tvRemark.setText(score.getRemark());
        }

        QuizActivity.questionsAttempted = 0;
        QuizActivity.quizScore = 0;
        QuizActivity.score = new Score();

        mInterStitialAd = new InterstitialAd(this);
        mInterStitialAd.setAdUnitId(ScoreActivity.this.getResources().getString(R.string.interstitial_ad_id));
        mInterStitialAd.loadAd(new AdRequest.Builder().build());

        mInterStitialAd.setAdListener(adListener);

        tvPlayAgain = findViewById(R.id.tv_play_again);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterStitialAd.isLoaded()) mInterStitialAd.show();
    }

    AdListener adListener = new AdListener(){
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            tvPlayAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInterStitialAd.show();
                }
            });
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            tvPlayAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ScoreActivity.this, QuizCategoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }
    };
}
