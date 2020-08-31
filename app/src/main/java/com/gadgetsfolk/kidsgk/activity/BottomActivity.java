package com.gadgetsfolk.kidsgk.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.fragment.QuizCategoryFragment;
import com.gadgetsfolk.kidsgk.fragment.QuizFragment;
import com.gadgetsfolk.kidsgk.fragment.VideoFragment;
import com.gadgetsfolk.kidsgk.helper.HelperMethods;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView tvToolbar;
    private QuizCategoryFragment quizCategoryFragment;
    private VideoFragment videoFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        ButterKnife.bind(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new HideBottomViewOnScrollBehavior());
        navigation.setOnNavigationItemSelectedListener(listener);
        tvToolbar.setText(getString(R.string.quiz));

        quizCategoryFragment = new QuizCategoryFragment();
        videoFragment = new VideoFragment();

        HelperMethods.loadFragment(quizCategoryFragment, this);

    }

    final BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        switch (item.getItemId()){
            case R.id.action_quiz:
                tvToolbar.setText(getString(R.string.quiz));
                HelperMethods.showFragment(quizCategoryFragment, this);
                HelperMethods.hideFragment(videoFragment, this);
                return true;
            case R.id.action_videos:
                tvToolbar.setText(getString(R.string.video));
                HelperMethods.showFragment(videoFragment, this);
                HelperMethods.hideFragment(quizCategoryFragment, this);
                return true;
        }
        return false;
    };
}
