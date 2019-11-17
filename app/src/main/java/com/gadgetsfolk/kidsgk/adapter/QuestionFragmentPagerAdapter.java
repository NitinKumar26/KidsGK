package com.gadgetsfolk.kidsgk.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gadgetsfolk.kidsgk.activity.QuizActivity;
import com.gadgetsfolk.kidsgk.fragment.QuizFragment;

public class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public QuestionFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setArguments(bundle);
        quizFragment.setArguments(bundle);
        return quizFragment;
    }

    @Override
    public int getCount() {
        return QuizActivity.mQuizList.size();
    }
}
