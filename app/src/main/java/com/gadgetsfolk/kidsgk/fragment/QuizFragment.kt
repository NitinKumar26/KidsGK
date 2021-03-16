package com.gadgetsfolk.kidsgk.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.activity.QuizActivity
import com.gadgetsfolk.kidsgk.activity.ScoreActivity
import com.gadgetsfolk.kidsgk.databinding.FragmentQuizBinding
import com.gadgetsfolk.kidsgk.model.Quiz
import com.google.android.gms.ads.AdRequest
import java.util.*

class QuizFragment : Fragment() {
    private var currentPageNumber = 0
    private var quiz: Quiz? = null
    private var mediaCorrect: MediaPlayer? = null
    private var mediaWrong: MediaPlayer? = null

    private var _binding: FragmentQuizBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) currentPageNumber = arguments!!.getInt("position")
        quiz = QuizActivity.mQuizList!![currentPageNumber]
        mediaCorrect = MediaPlayer.create(context, R.raw.correct)
        mediaWrong = MediaPlayer.create(context, R.raw.wrong)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        val options = ArrayList<String?>()
        options.add(quiz!!.option_one)
        options.add(quiz!!.option_two)
        options.add(quiz!!.option_three)
        options.add(quiz!!.option_four)
        options.shuffle()
        binding.tvQuestion.text = quiz!!.question //Set Question
        binding.tvOptionOne.text = options[0] //Set Option One
        binding.tvOptionTwo.text = options[1] //Set Option Two
        binding.tvOptionThree.text = options[2] //Set Option Three
        binding.tvOptionFour.text = options[3] //Set Option Four

        Glide.with(view.context).load(quiz!!.img_url).placeholder(R.color.colorGrey).into(binding.imgQuestion)

        mediaWrong?.setOnCompletionListener {
            mediaWrong?.release()
            QuizActivity.nonViewPager?.currentItem = currentPageNumber + 1
        }

        mediaCorrect?.setOnCompletionListener {
            mediaCorrect?.release()
            QuizActivity.nonViewPager?.currentItem = currentPageNumber + 1
        }

        binding.tvOptionOne.setOnClickListener{ onOptionOneSelected() }
        binding.tvOptionTwo.setOnClickListener{ onOptionTwoSelected() }
        binding.tvOptionThree.setOnClickListener{ onOptionThreeSelected() }
        binding.tvOptionFour.setOnClickListener{ onOptionFourSelected() }

    }

    private fun onOptionOneSelected() {
        if (binding.tvOptionOne.text.toString() == quiz!!.answer) {
            binding.tvOptionOne.setBackgroundResource(R.drawable.bg_answer)
            binding.tvOptionTwo.setOnClickListener(null)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionFour.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            binding.tvOptionOne.setBackgroundResource(R.drawable.bg_wrong_answer)
            binding.tvOptionTwo.setOnClickListener(null)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionFour.setOnClickListener(null)
            mediaWrong!!.start()
            //QuizActivity.viewPager.setCurrentItem(currentPageNumber + 1);
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionTwoSelected() {
        if (binding.tvOptionTwo.text.toString() == quiz!!.answer) {
            binding.tvOptionTwo.setBackgroundResource(R.drawable.bg_answer)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionFour.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            binding.tvOptionTwo.setBackgroundResource(R.drawable.bg_wrong_answer)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionFour.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
            mediaWrong!!.start()
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionThreeSelected() {
        if (binding.tvOptionThree.text.toString() == quiz!!.answer) {
            binding.tvOptionThree.setBackgroundResource(R.drawable.bg_answer)
            binding.tvOptionFour.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
            binding.tvOptionTwo.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            binding.tvOptionThree.setBackgroundResource(R.drawable.bg_wrong_answer)
            binding.tvOptionFour.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
            binding.tvOptionTwo.setOnClickListener(null)
            mediaWrong!!.start()
        }
        setScore(QuizActivity.quizScore)
    }

    private fun onOptionFourSelected() {
        if (binding.tvOptionFour.text.toString() == quiz!!.answer) {
            binding.tvOptionFour.setBackgroundResource(R.drawable.bg_answer)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionTwo.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
            mediaCorrect!!.start()
            QuizActivity.quizScore += 1
        } else {
            binding.tvOptionFour.setBackgroundResource(R.drawable.bg_wrong_answer)
            binding.tvOptionThree.setOnClickListener(null)
            binding.tvOptionTwo.setOnClickListener(null)
            binding.tvOptionOne.setOnClickListener(null)
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