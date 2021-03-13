package com.gadgetsfolk.kidsgk.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.adapter.QuestionFragmentPagerAdapter
import com.gadgetsfolk.kidsgk.databinding.ActivityQuizBinding
import com.gadgetsfolk.kidsgk.helper.HelperMethods.pix
import com.gadgetsfolk.kidsgk.model.Quiz
import com.gadgetsfolk.kidsgk.model.QuizType
import com.gadgetsfolk.kidsgk.model.Score
import com.gadgetsfolk.kidsgk.utils.NonSwipeableViewpager
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class QuizActivity : AppCompatActivity() {

    private var mQuizType: QuizType? = null
    private var mQuizSubType: QuizType? = null
    private lateinit var binding: ActivityQuizBinding

    override fun onBackPressed() {
        super.onBackPressed()
        score = Score()
        questionsAttempted = 0
        quizScore = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mQuizType = intent.getParcelableExtra("quiz_type")
        mQuizSubType = intent.getParcelableExtra("quiz_sub_type")

        nonViewPager = binding.viewPager

        mQuizList = ArrayList()
        questions
        binding.viewPager.pageMargin = pix(this@QuizActivity, 24)
        binding.viewPager.clipToPadding = false
        binding.viewPager.setPadding(pix(this@QuizActivity, 20), pix(this@QuizActivity, 0),
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
                        binding.viewPager.adapter = questionFragmentPagerAdapter //Set the DealSliderAdapter with Viewpager;
                        binding.viewPager.offscreenPageLimit = mQuizList!!.size + 1
                        binding.indicator.setupWithViewPager(binding.viewPager)
                    }.addOnFailureListener {
                        Toast.makeText(this@QuizActivity, "Unable to get questions.", Toast.LENGTH_LONG).show() }
                    .addOnCompleteListener {
                        binding.progressCircular.visibility = View.GONE }
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