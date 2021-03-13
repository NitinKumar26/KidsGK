package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter
import com.gadgetsfolk.kidsgk.databinding.ActivityQuizSubCategoryBinding
import com.gadgetsfolk.kidsgk.helper.HelperMethods.ClickListener
import com.gadgetsfolk.kidsgk.helper.HelperMethods.RecyclerTouchListener
import com.gadgetsfolk.kidsgk.model.QuizType
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class QuizSubCategoryActivity : AppCompatActivity(), ClickListener {

    private var quizTypeAdapter: QuizTypeAdapter? = null
    private var quizType: QuizType? = null
    private var interstitialAd: InterstitialAd? = null

    //List of videoItems and native ads that populate the RecyclerView.
    private val mRecyclerViewItems: MutableList<QuizType> = ArrayList()
    private lateinit var binding: ActivityQuizSubCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizSubCategoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        quizType = intent.getParcelableExtra("quiz_type")
        setSupportActionBar(binding.toolbarMain)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        if (actionBar != null) if (quizType != null) actionBar.setTitle(quizType!!.title)
        quizTypeAdapter = QuizTypeAdapter(this@QuizSubCategoryActivity, mRecyclerViewItems)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@QuizSubCategoryActivity, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = quizTypeAdapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this@QuizSubCategoryActivity, RecyclerView.VERTICAL))
        binding.recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, this))
        quizSubCategories
        interstitialAd = InterstitialAd(this)
        interstitialAd!!.adUnitId = this@QuizSubCategoryActivity.resources.getString(R.string.interstitial_ad_id)
        interstitialAd!!.loadAd(AdRequest.Builder().build())
    }

    //Collections.shuffle(mQuizSubTypeList);
    private val quizSubCategories: Unit get() {
            FirebaseFirestore.getInstance()
                .collection("quiz_type")
                .document(quizType?.doc_id!!)
                .collection("quiz_sub_type").orderBy("title")
                .get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                    val types = queryDocumentSnapshots.toObjects(QuizType::class.java)
                    mRecyclerViewItems.addAll(types)
                    quizTypeAdapter!!.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(this@QuizSubCategoryActivity, "Unable to get quizzes", Toast.LENGTH_LONG).show()
                }.addOnCompleteListener { binding.progressCircular.visibility = View.GONE }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (interstitialAd!!.isLoaded) {
            interstitialAd!!.show()
        }
    }

    override fun onClick(position: Int) {
        if (quizTypeAdapter!!.getItemViewType(position) == 0) {
            val quizSubType = mRecyclerViewItems[position] as QuizType
            val intent = Intent(this@QuizSubCategoryActivity, QuizActivity::class.java)
            intent.putExtra("quiz_type", quizType)
            intent.putExtra("quiz_sub_type", quizSubType)
            startActivity(intent)
        }
    }
}