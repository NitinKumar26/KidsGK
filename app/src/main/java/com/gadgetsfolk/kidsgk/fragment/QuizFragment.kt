package com.gadgetsfolk.kidsgk.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.activity.QuizActivity
import com.gadgetsfolk.kidsgk.activity.ScoreActivity
import com.gadgetsfolk.kidsgk.model.Quiz
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_quiz.*
import java.util.*

class QuizFragment : Fragment() {
    private var currentPageNumber = 0
    private var quiz: Quiz? = null
    private var mediaCorrect: MediaPlayer? = null
    private var mediaWrong: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) currentPageNumber = arguments!!.getInt("position")
        quiz = QuizActivity.mQuizList!![currentPageNumber]
        mediaCorrect = MediaPlayer.create(context, R.raw.correct)
        mediaWrong = MediaPlayer.create(context, R.raw.wrong)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        val options = ArrayList<String?>()
        options.add(quiz!!.option_one)
        options.add(quiz!!.option_two)
        options.add(quiz!!.option_three)
        options.add(quiz!!.option_four)
        options.shuffle()
        tv_question.text = quiz!!.question //Set Question
        tv_option_one.text = options[0] //Set Option One
        tv_option_two.text = options[1] //Set Option Two
        tv_option_three.text = options[2] //Set Option Three
        tv_option_four.text = options[3] //Set Option Four

        Glide.with(view.context).load(quiz!!.img_url).placeholder(R.color.colorGrey).into(img_question)

        mediaWrong?.setOnCompletionListener {
            mediaWrong?.release()
            QuizActivity.nonViewPager?.currentItem = currentPageNumber + 1
        }

        mediaCorrect?.setOnCompletionListener {
            mediaCorrect?.release()
            QuizActivity.nonViewPager?.currentItem = currentPageNumber + 1
        }

        tv_option_one.setOnClickListener{ onOptionOneSelected() }
        tv_option_two.setOnClickListener{ onOptionTwoSelected() }
        tv_option_three.setOnClickListener{ onOptionThreeSelected() }
        tv_option_four.setOnClickListener{ onOptionFourSelected() }

    }

    private fun onOptionOneSelected() {
        if (tv_option_one.text.toString() == quiz!!.answer) {
            tv_option_one.setBackgroundResource(R.drawable.bg_answer)
            tv_option_two.setOnClickListener(null)
            tv_option_three.setOnClickListener(null)
            tv_option_four.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            tv_option_one.setBackgroundResource(R.drawable.bg_wrong_answer)
            tv_option_two.setOnClickListener(null)
            tv_option_three.setOnClickListener(null)
            tv_option_four.setOnClickListener(null)
            mediaWrong!!.start()
            //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionTwoSelected() {
        if (tv_option_two.text.toString() == quiz!!.answer) {
            tv_option_two.setBackgroundResource(R.drawable.bg_answer)
            tv_option_three.setOnClickListener(null)
            tv_option_four.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            tv_option_two.setBackgroundResource(R.drawable.bg_wrong_answer)
            tv_option_three.setOnClickListener(null)
            tv_option_four.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            mediaWrong!!.start()
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionThreeSelected() {
        if (tv_option_three.text.toString() == quiz!!.answer) {
            tv_option_three.setBackgroundResource(R.drawable.bg_answer)
            tv_option_four.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            tv_option_two.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            tv_option_three.setBackgroundResource(R.drawable.bg_wrong_answer)
            tv_option_four.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            tv_option_two.setOnClickListener(null)
            mediaWrong!!.start()
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionFourSelected() {
        if (tv_option_four.text.toString() == quiz!!.answer) {
            tv_option_four.setBackgroundResource(R.drawable.bg_answer)
            tv_option_three.setOnClickListener(null)
            tv_option_two.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            tv_option_four.setBackgroundResource(R.drawable.bg_wrong_answer)
            tv_option_three.setOnClickListener(null)
            tv_option_two.setOnClickListener(null)
            tv_option_one.setOnClickListener(null)
            mediaWrong!!.start()
        }
        setScore(QuizActivity.quizScore)
    }

    private fun setScore(score: Int) {
        QuizActivity.questionsAttempted += 1
        if (QuizActivity.questionsAttempted == QuizActivity.mQuizList!!.size) {
            QuizActivity.score.correct_questions = score.toString()
            QuizActivity.score.total_questions = QuizActivity.mQuizList!!.size.toString()
            val percentage = QuizActivity.quizScore / QuizActivity.score.total_questions!!.toInt() * 100
            QuizActivity.score.percentage = percentage.toString() //Set Percentage
            //Give remark according to percentage
            when (percentage) {
                in 76..100 -> QuizActivity.score.remark = "Excellent!"
                in 61..75 -> QuizActivity.score.remark = "Can do better"
                in 51..60 -> QuizActivity.score.remark = "Keep it up"
                in 41..50 -> QuizActivity.score.remark = "Practice more"
                else -> QuizActivity.score.remark = "Learn and play more quizzes"
            }
            val intent = Intent(context, ScoreActivity::class.java)
            intent.putExtra("score_obj", QuizActivity.score)
            startActivity(intent)
            if (activity != null) activity!!.finish()
        }
    }
}