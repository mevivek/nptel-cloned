package com.nptel.courses.online.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.nptel.courses.online.databinding.SearchActivityBinding
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.entities.Video
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.retrofit.RetrofitFactory
import com.nptel.courses.online.ui.SearchActivity
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment
import com.nptel.courses.online.ui.playlist.SearchVideoListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: SearchActivityBinding
    private lateinit var videoAdapter: SearchVideoListAdapter
    private var videos: List<Video>? = null
    private val searchResultFor: String? = null
    private var courseId: String? = null
    private var courseName: String? = null
    private var currentCall: Call<List<Video>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        videoAdapter = SearchVideoListAdapter(object : ListItemClickListener<Video> {
            override fun onClick(video: Video) {
                val intent = Intent(this@SearchActivity, PlaylistActivity::class.java)
                intent.putExtra(PlaylistActivity.ID, video.id)
                intent.putExtra(PlaylistActivity.COURSE_NAME, "Searched Video")
                intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.SEARCHED.toString())
                startActivity(intent)
            }

            override fun onOptionClicked(video: Video, optionId: Int) {}
        })
        binding!!.recyclerView.adapter = videoAdapter
        binding!!.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                fetch(newText)
                return false
            }
        })
        binding!!.search.requestFocus()
        binding!!.search.setIconifiedByDefault(false)
        courseId = intent.getStringExtra("course_id")
        courseName = intent.getStringExtra("course_name")
        if (courseName != null) binding!!.courseName.isChecked = true
        if (courseName != null) binding!!.courseName.text = Html.fromHtml("In course <b>$courseName</b>") else binding!!.courseName.visibility = View.GONE
        binding!!.courseName.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> fetch(binding!!.search.query.toString()) }
    }

    private fun fetch(query: String) {
        if (currentCall != null) currentCall!!.cancel()
        if (query.isEmpty()) {
            videoAdapter!!.submitList(null)
            binding!!.searchResultFor.text = null
            binding!!.hint.text = "Your results will appear here."
            binding!!.hint.visibility = View.VISIBLE
            return
        }
//        currentCall = RetrofitFactory.getRetrofitService(this).searchVideos(query, if (binding!!.courseName.isChecked) courseId else null)
        currentCall!!.enqueue(object : Callback<List<Video>> {
            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                if (response.isSuccessful) {
                    binding!!.searchResultFor.text = Html.fromHtml("Results for <b>$query</b>")
                    videos = response.body()
                    videoAdapter!!.submitList(videos)
                    if (videos!!.isEmpty()) {
                        binding!!.hint.text = "No video found."
                        binding!!.hint.visibility = View.VISIBLE
                    } else binding!!.hint.visibility = View.GONE
                } else Toast.makeText(this@SearchActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Video>>, throwable: Throwable) {
                if (throwable.message != null && !throwable.message.equals("canceled", ignoreCase = true)) Toast.makeText(this@SearchActivity, throwable.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        fun start(context: Context, course: Course?) {
            val intent = Intent(context, SearchActivity::class.java)
            if (course != null) {
                intent.putExtra("course_id", course.id)
                intent.putExtra("course_name", course.title)
            }
            context.startActivity(intent)
        }
    }
}