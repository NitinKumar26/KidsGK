package com.gadgetsfolk.kidsgk.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfolk.kidsgk.activity.QuizSubCategoryActivity
import com.gadgetsfolk.kidsgk.adapter.QuizTypeAdapter
import com.gadgetsfolk.kidsgk.databinding.FragmentQuizCategoryBinding
import com.gadgetsfolk.kidsgk.helper.HelperMethods
import com.gadgetsfolk.kidsgk.helper.HelperMethods.RecyclerTouchListener
import com.gadgetsfolk.kidsgk.model.QuizType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class QuizCategoryFragment : Fragment(), HelperMethods.ClickListener {

    private var adapter: QuizTypeAdapter? = null

    private val mRecyclerViewItems: MutableList<QuizType> = ArrayList()

    private var _binding: FragmentQuizCategoryBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentQuizCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = QuizTypeAdapter(context!!, mRecyclerViewItems)

        if (context != null) {
            binding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnItemTouchListener(RecyclerTouchListener(context, this))

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
                .addOnCompleteListener { binding.progressCircular.visibility = View.GONE }
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