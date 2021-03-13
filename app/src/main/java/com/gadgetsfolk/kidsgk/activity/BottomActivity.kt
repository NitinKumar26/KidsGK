package com.gadgetsfolk.kidsgk.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.databinding.ActivityBottomBinding
import com.gadgetsfolk.kidsgk.fragment.QuizCategoryFragment
import com.gadgetsfolk.kidsgk.fragment.VideoFragment
import com.gadgetsfolk.kidsgk.helper.HelperMethods.hideFragment
import com.gadgetsfolk.kidsgk.helper.HelperMethods.loadFragment
import com.gadgetsfolk.kidsgk.helper.HelperMethods.showFragment
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomActivity : AppCompatActivity() {

    private var quizCategoryFragment: QuizCategoryFragment? = null
    private var videoFragment: VideoFragment? = null

    private lateinit var binding: ActivityBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.itemIconTintList = null
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = HideBottomViewOnScrollBehavior<View?>()
        navigation.setOnNavigationItemSelectedListener(listener)
        binding.toolbarTitle.text = getString(R.string.quiz)
        quizCategoryFragment = QuizCategoryFragment()
        videoFragment = VideoFragment()
        loadFragment(quizCategoryFragment, this)
    }

    val listener = BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.action_quiz -> {
                binding.toolbarTitle.text = getString(R.string.quiz)
                showFragment(quizCategoryFragment!!, this)
                hideFragment(videoFragment!!, this)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_videos -> {
                binding.toolbarTitle.text = getString(R.string.video)
                showFragment(videoFragment!!, this)
                hideFragment(quizCategoryFragment!!, this)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}