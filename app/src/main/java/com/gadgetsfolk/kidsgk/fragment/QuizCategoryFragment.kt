package com.gadgetsfolk.kidsgk.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfolk.kidsgk.R
import com.gadgetsfolk.kidsgk.activity.QuizSubCategoryActivity
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter
import com.gadgetsfolk.kidsgk.helper.HelperMethods
import com.gadgetsfolk.kidsgk.helper.HelperMethods.RecyclerTouchListener
import com.gadgetsfolk.kidsgk.model.QuizType
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_quiz_category.*
import java.util.*

class QuizCategoryFragment : Fragment(), HelperMethods.ClickListener {

    private var adapter: QuizTypeAdapter? = null

    //The AdLoader used to load ads
    private var adLoader: AdLoader? = null

    //List of quizItems and native ads that populate the RecyclerView;
    private val mRecyclerViewItems: MutableList<Any?> = ArrayList()

    //List of nativeAds that have been successfully loaded.
    private val mNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = QuizTypeAdapter(context!!, mRecyclerViewItems)

        if (context != null) {
            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        recyclerView.adapter = adapter

        recyclerView.addOnItemTouchListener(RecyclerTouchListener(context, this))

        quizCategories
    }

    private val quizCategories: Unit get() {
            FirebaseFirestore.getInstance().collection("quiz_type").get()
                    .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val types = queryDocumentSnapshots.toObjects(QuizType::class.java)
                        mRecyclerViewItems.addAll(types)
                        mRecyclerViewItems.shuffle()
                        adapter!!.setItems(mRecyclerViewItems)
                        adapter!!.notifyDataSetChanged()
                        loadNativeAds()
                    }
                    .addOnFailureListener { e: Exception? -> Toast.makeText(context, "Unable to get quizzes", Toast.LENGTH_LONG).show() }
                    .addOnCompleteListener { task: Task<QuerySnapshot?>? -> progress_circular.visibility = View.GONE }
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
            adapter!!.setItems(mRecyclerViewItems)
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun loadNativeAds() {
        if (context != null) {
            val builder = AdLoader.Builder(context, getString(R.string.native_ad_unit))
            adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
                // A native ad loaded successfully, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader!!.isLoading) {
                    insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                    //adapter.notifyDataSetChanged();
                }
            }.withAdListener(
                    object : AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.")
                            if (!adLoader!!.isLoading) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                                //adapter.notifyDataSetChanged();
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
    }

    override fun onClick(position: Int) {
        if (adapter!!.getItemViewType(position) == 0) {
            val quizType = mRecyclerViewItems[position] as QuizType?
            val intent = Intent(context, QuizSubCategoryActivity::class.java)
            intent.putExtra("quiz_type", quizType)
            startActivity(intent)
        }
    }
}