package com.gadgetsfolk.kidsgk.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gadgetsfolk.kidsgk.activity.QuizActivity
import com.gadgetsfolk.kidsgk.fragment.QuizFragment

class QuestionFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("position", position)
        val quizFragment = QuizFragment()
        quizFragment.arguments = bundle
        quizFragment.arguments = bundle
        return quizFragment
    }

    override fun getCount(): Int {
        return QuizActivity.mQuizList!!.size
    }
}