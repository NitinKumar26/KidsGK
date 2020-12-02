package com.gadgetsfolk.kidsgk.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter
import com.gadgetsfolk.kidsgk.helper.HelperMethods.ClickListener
import com.gadgetsfolk.kidsgk.helper.HelperMethods.RecyclerTouchListener
import com.gadgetsfolk.kidsgk.model.QuizType
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_quiz_sub_category.*
import java.util.*

class QuizSubCategoryActivity : AppCompatActivity(), ClickListener {

    private var quizTypeAdapter: QuizTypeAdapter? = null
    private var quizType: QuizType? = null
    private var interstitialAd: InterstitialAd? = null

    //The AdLoader used to load ads
    private var adLoader: AdLoader? = null

    //List of videoItems and native ads that populate the RecyclerView.
    private val mRecyclerViewItems: MutableList<Any?> = ArrayList()

    //List of native ads that have been successfully loaded.
    private val mNativeAds: MutableList<UnifiedNativeAd> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_sub_category)
        ButterKnife.bind(this)

        quizType = intent.getParcelableExtra("quiz_type")
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        if (actionBar != null) if (quizType != null) actionBar.setTitle(quizType!!.title)
        quizTypeAdapter = QuizTypeAdapter(this@QuizSubCategoryActivity, mRecyclerViewItems)
        recyclerView!!.layoutManager = LinearLayoutManager(this@QuizSubCategoryActivity, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = quizTypeAdapter
        recyclerView!!.addItemDecoration(DividerItemDecoration(this@QuizSubCategoryActivity, RecyclerView.VERTICAL))
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, this))
        quizSubCategories
        interstitialAd = InterstitialAd(this)
        interstitialAd!!.adUnitId = this@QuizSubCategoryActivity.resources.getString(R.string.interstitial_ad_id)
        interstitialAd!!.loadAd(AdRequest.Builder().build())
    }

    //Collections.shuffle(mQuizSubTypeList);
    private val quizSubCategories: Unit
        get() {
            FirebaseFirestore.getInstance().collection("quiz_type")
                    .document(quizType?.doc_id!!)
                    .collection("quiz_sub_type").orderBy("title")
                    .get()
                    .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val types = queryDocumentSnapshots.toObjects(QuizType::class.java)
                        mRecyclerViewItems.addAll(types)
                        quizTypeAdapter!!.setItems(mRecyclerViewItems)
                        quizTypeAdapter!!.notifyDataSetChanged()
                        loadNativeAds()
                    }.addOnFailureListener {
                        Toast.makeText(this@QuizSubCategoryActivity, "Unable to get quizzes", Toast.LENGTH_LONG).show() }
                    .addOnCompleteListener { progress_circular.visibility = View.GONE }
        }

    private fun insertAdsInMenuItems(mNativeAds: List<UnifiedNativeAd>, mRecyclerViewItems: MutableList<Any?>) {
        if (mNativeAds.isEmpty()) {
            return
        }
        val offset = mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad)
            index += offset
            quizTypeAdapter!!.setItems(mRecyclerViewItems)
            quizTypeAdapter!!.notifyDataSetChanged()
        }
    }

    private fun loadNativeAds() {
        val builder = AdLoader.Builder(this, getString(R.string.native_ad_unit))
        adLoader = builder.forUnifiedNativeAd { unifiedNativeAd: UnifiedNativeAd ->
            // A native ad loaded successfully, check if the ad loader has finished loading
            // and if so, insert the ads into the list.
            mNativeAds.add(unifiedNativeAd)
            if (!adLoader!!.isLoading) {
                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
            }
        }.withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.")
                        if (!adLoader!!.isLoading) {
                            insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                        }
                    }

                    override fun onAdClicked() {
                        //super.onAdClicked();
                        //Ad Clicked
                        Log.e("adclicked", "yes")
                    }
                }).build()

        //Number of Native Ads to load
        val NUMBER_OF_ADS: Int
        NUMBER_OF_ADS = if (mRecyclerViewItems.size <= 9) 3 else {
            mRecyclerViewItems.size / 5 + 1
        }

        // Load the Native ads.
        adLoader!!.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS)
        Log.e("numberOfAds", NUMBER_OF_ADS.toString())
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