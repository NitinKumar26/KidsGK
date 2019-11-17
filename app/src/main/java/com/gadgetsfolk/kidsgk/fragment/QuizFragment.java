package com.gadgetsfolk.kidsgk.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.gadgetsfolk.kidsgk.R;
import com.gadgetsfolk.kidsgk.activity.QuizActivity;
import com.gadgetsfolk.kidsgk.activity.ScoreActivity;
import com.gadgetsfolk.kidsgk.model.Quiz;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rishabhharit.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.Collections;

public class QuizFragment extends Fragment implements View.OnClickListener{
    private int currentPageNumber;
    private Quiz quiz;
    private TextView tvQuestion, tvOptionOne, tvOptionTwo, tvOptionThree, tvOptionFour;
    private RoundedImageView imgQuestion;
    private AdView mAdView;
    private MediaPlayer mediaCorrect;
    private MediaPlayer mediaWrong;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) currentPageNumber = getArguments().getInt("position");

        quiz = QuizActivity.mQuizList.get(currentPageNumber);

        tvQuestion = view.findViewById(R.id.tv_question);
        imgQuestion = view.findViewById(R.id.img_question);
        tvOptionOne = view.findViewById(R.id.tv_option_one);
        tvOptionTwo = view.findViewById(R.id.tv_option_two);
        tvOptionThree = view.findViewById(R.id.tv_option_three);
        tvOptionFour = view.findViewById(R.id.tv_option_four);

        mediaCorrect = MediaPlayer.create(getContext(), R.raw.correct);
        mediaWrong = MediaPlayer.create(getContext(), R.raw.wrong);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ArrayList<String> options = new ArrayList<>();
        options.add(quiz.getOption_one());
        options.add(quiz.getOption_two());
        options.add(quiz.getOption_three());
        options.add(quiz.getOption_four());

        Collections.shuffle(options);

        tvQuestion.setText(quiz.getQuestion()); //Set Question
        tvOptionOne.setText(options.get(0)); //Set Option One
        tvOptionTwo.setText(options.get(1)); //Set Option Two
        tvOptionThree.setText(options.get(2)); //Set Option Three
        tvOptionFour.setText(options.get(3)); //Set Option Four

        Glide.with(view.getContext()).load(quiz.getImg_url()).placeholder(R.color.colorGrey).into(imgQuestion);

        tvOptionOne.setOnClickListener(this);
        tvOptionTwo.setOnClickListener(this);
        tvOptionThree.setOnClickListener(this);
        tvOptionFour.setOnClickListener(this);

        mediaWrong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaWrong.release();
                QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
            }
        });

        mediaCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaCorrect.release();
                QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_option_one:
                if (tvOptionOne.getText().toString().equals(quiz.getAnswer())) {
                    tvOptionOne.setBackgroundResource(R.drawable.bg_answer);
                    tvOptionTwo.setOnClickListener(null);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionFour.setOnClickListener(null);
                    mediaCorrect.start();
                    QuizActivity.quizScore += 1;
                    setScore(QuizActivity.quizScore);
                }else{
                    tvOptionOne.setBackgroundResource(R.drawable.bg_wrong_answer);
                    tvOptionTwo.setOnClickListener(null);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionFour.setOnClickListener(null);
                    mediaWrong.start();
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }
                break;
            case R.id.tv_option_two:
                if (tvOptionTwo.getText().toString().equals(quiz.getAnswer())) {
                    tvOptionTwo.setBackgroundResource(R.drawable.bg_answer);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionFour.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    mediaCorrect.start();
                    QuizActivity.quizScore += 1;
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }else{
                    tvOptionTwo.setBackgroundResource(R.drawable.bg_wrong_answer);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionFour.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    mediaWrong.start();
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }
                break;
            case R.id.tv_option_three:
                if (tvOptionThree.getText().toString().equals(quiz.getAnswer())) {
                    tvOptionThree.setBackgroundResource(R.drawable.bg_answer);
                    tvOptionFour.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    tvOptionTwo.setOnClickListener(null);
                    mediaCorrect.start();
                    QuizActivity.quizScore += 1;
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }else{
                    tvOptionThree.setBackgroundResource(R.drawable.bg_wrong_answer);
                    tvOptionFour.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    tvOptionTwo.setOnClickListener(null);
                    mediaWrong.start();
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }
                break;
            case R.id.tv_option_four:
                if (tvOptionFour.getText().toString().equals(quiz.getAnswer())) {
                    tvOptionFour.setBackgroundResource(R.drawable.bg_answer);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionTwo.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    mediaCorrect.start();
                    QuizActivity.quizScore += 1;
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }else{
                    tvOptionFour.setBackgroundResource(R.drawable.bg_wrong_answer);
                    tvOptionThree.setOnClickListener(null);
                    tvOptionTwo.setOnClickListener(null);
                    tvOptionOne.setOnClickListener(null);
                    mediaWrong.start();
                    setScore(QuizActivity.quizScore);
                    //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
                }
                break;
        }

    }

    private void setScore(int score){
        QuizActivity.questionsAttempted += 1;
        Log.e("att", String.valueOf(QuizActivity.questionsAttempted));
        if (QuizActivity.questionsAttempted == QuizActivity.mQuizList.size()){
            QuizActivity.score.setCorrect_questions(String.valueOf(score));
            QuizActivity.score.setTotal_questions(String.valueOf(QuizActivity.mQuizList.size()));

            int percentage = (QuizActivity.quizScore / Integer.parseInt(QuizActivity.score.getTotal_questions())) * 100;
            QuizActivity.score.setPercentage(String.valueOf(percentage)); //Set Percentage
            //Give remark according to percentage
            if (percentage <= 100 && percentage > 75)
                QuizActivity.score.setRemark("Excellent!");
            else if (percentage <= 75 && percentage > 60)
                QuizActivity.score.setRemark("Can do better");
            else if (percentage <= 60 && percentage > 50)
                QuizActivity.score.setRemark("Keep it up");
            else if (percentage <= 50 && percentage > 40)
                QuizActivity.score.setRemark("Practice more");
            else
                QuizActivity.score.setRemark("Learn and play more quizzes");

            Intent intent = new Intent(getContext(), ScoreActivity.class);
            intent.putExtra("score_obj", QuizActivity.score);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        }
    }
}
