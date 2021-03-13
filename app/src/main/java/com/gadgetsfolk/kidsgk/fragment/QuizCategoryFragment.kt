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

    private val mRecyclerViewItems: MutableList<QuizType> = ArrayList()

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
            FirebaseFirestore.getInstance()
                .collection("quiz_type").get()
                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val types = queryDocumentSnapshots.toObjects(QuizType::class.java)
                        mRecyclerViewItems.addAll(types)
                        mRecyclerViewItems.shuffle()
                        adapter!!.notifyDataSetChanged()
                }
                .addOnFailureListener { e: Exception? -> Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show() }
                .addOnCompleteListener { progress_circular.visibility = View.GONE }
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