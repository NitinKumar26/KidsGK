package com.gadgetsfolk.kidsgk.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.activity.QuizActivity
import com.gadgetsfolk.kidsgk.activity.QuizActivity.Companion.nonViewPager
import com.gadgetsfolk.kidsgk.adapter.QuestionFragmentPagerAdapter
import com.gadgetsfolk.kidsgk.helper.HelperMethods.pix
import com.gadgetsfolk.kidsgk.model.Quiz
import com.gadgetsfolk.kidsgk.model.QuizType
import com.gadgetsfolk.kidsgk.model.Score
import com.gadgetsfolk.kidsgk.utils.NonSwipeableViewpager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*

class QuizActivity : AppCompatActivity() {

    private var mQuizType: QuizType? = null
    private var mQuizSubType: QuizType? = null

    override fun onBackPressed() {
        super.onBackPressed()
        score = Score()
        questionsAttempted = 0
        quizScore = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        mQuizType = intent.getParcelableExtra("quiz_type")
        mQuizSubType = intent.getParcelableExtra("quiz_sub_type")

        nonViewPager = viewPager

        mQuizList = ArrayList()
        questions
        viewPager.pageMargin = pix(this@QuizActivity, 24)
        viewPager.clipToPadding = false
        viewPager.setPadding(pix(this@QuizActivity, 20), pix(this@QuizActivity, 0),
                pix(this@QuizActivity, 20), pix(this@QuizActivity, 20))
    }

    //Set the DealSliderAdapter with Viewpager;
    private val questions: Unit
        get() {
            FirebaseFirestore.getInstance().collection("quiz_type")
                    .document(mQuizType?.doc_id!!)
                    .collection("quiz_sub_type")
                    .document(mQuizSubType?.doc_id!!)
                    .collection("questions").get()
                    .addOnSuccessListener { queryDocumentSnapshots ->
                        val questions = queryDocumentSnapshots.toObjects(Quiz::class.java)
                        mQuizList!!.addAll(questions)
                        mQuizList!!.shuffle()
                        val questionFragmentPagerAdapter = QuestionFragmentPagerAdapter(supportFragmentManager)
                        viewPager.adapter = questionFragmentPagerAdapter //Set the DealSliderAdapter with Viewpager;
                        viewPager!!.offscreenPageLimit = mQuizList!!.size + 1
                        indicator.setupWithViewPager(viewPager)
                    }.addOnFailureListener {
                        Toast.makeText(this@QuizActivity, "Unable to get questions.", Toast.LENGTH_LONG).show() }
                    .addOnCompleteListener {
                        progress_circular.visibility = View.GONE }
        }

    companion object {
        @JvmField
        var nonViewPager: NonSwipeableViewpager? = null
        @JvmField
        var mQuizList: ArrayList<Quiz?>? = null
        @JvmField
        var score = Score()
        @JvmField
        var questionsAttempted = 0
        @JvmField
        var quizScore = 0
    }
}