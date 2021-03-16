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
import com.gadgetsfolk.kidsgk.activity.PlayerActivity
import com.gadgetsfolk.kidsgk.adapter.VideoAdapter
import com.gadgetsfolk.kidsgk.databinding.FragmentVideosBinding
import com.gadgetsfolk.kidsgk.helper.HelperMethods.ClickListener
import com.gadgetsfolk.kidsgk.helper.HelperMethods.RecyclerTouchListener
import com.gadgetsfolk.kidsgk.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class VideoFragment : Fragment(), ClickListener {

    private var videoAdapter: VideoAdapter? = null

    //List of videoItems and native ads that populate the RecyclerView.
    private val mRecyclerViewItems: MutableList<Any?> = ArrayList()

    private var _binding: FragmentVideosBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        videoAdapter = VideoAdapter(context!!, mRecyclerViewItems)
        if (context != null) {
            binding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
        binding.recyclerView.adapter = videoAdapter
        binding.recyclerView.addOnItemTouchListener(RecyclerTouchListener(context, this))
        videos
    }

    private val videos: Unit get() {
            FirebaseFirestore.getInstance()
                .collection("videos").get()
                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                    val types = queryDocumentSnapshots.toObjects(Video::class.java)
                    mRecyclerViewItems.addAll(types)
                    mRecyclerViewItems.shuffle()
                    videoAdapter?.setItems(mRecyclerViewItems)
                    videoAdapter?.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(context, "Unable to get quizzes", Toast.LENGTH_LONG).show()
                }.addOnCompleteListener {
                    binding.progressCircular.visibility = View.GONE
            }
    }

    override fun onClick(position: Int) {
        if (videoAdapter!!.getItemViewType(position) == 0) {
            val video = mRecyclerViewItems[position] as Video?
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("videoID", video!!.video_link)
            startActivity(intent)
        }
    }
}