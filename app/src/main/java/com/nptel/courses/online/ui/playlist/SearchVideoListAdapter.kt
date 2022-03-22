package com.nptel.courses.online.ui.playlist

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.databinding.SearchVideoItemBinding
import com.nptel.courses.online.databinding.VideoListItemBinding
import com.nptel.courses.online.entities.Video
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.ShimmerViewHolder

class SearchVideoListAdapter(private val clickListener: ListItemClickListener<Video>) : ListAdapter<Video, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.youtubeVideoId == oldItem.youtubeVideoId
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.title == oldItem.title && newItem.description == oldItem.description
    }
}) {
    private lateinit var colors: IntArray
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) VideoViewHolder(SearchVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) else ShimmerViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder) holder.bind(getItem(position))
    }

    internal inner class VideoViewHolder(private val binding: SearchVideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var color = 0
        fun bind(video: Video?) {
            binding.video = video
            color = getColor(adapterPosition)
            binding.videoTitle.setTextColor(color)
            binding.videoDuration.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.videoDuration.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
            binding.coursePlaylist.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            //            binding.coursePlaylist.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"));
            val root = binding.root as MaterialCardView
            root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color))))
            root.strokeColor = ColorUtility.getLighterVersion(color)
            binding.executePendingBindings()
            for (highlight in video!!.search!!.highlights!!) {
                highlight(highlight)
            }
        }

        private fun getColor(position: Int): Int {
//            if (colors != null && colors!!.size > position) return colors!![position]
//            colors = ColorUtility.getRandomColors(150, 150, 150, currentList.size, colors)
            return colors.get(position)
        }

        private fun highlight(highlight: Video.Highlight) {
            var start = -1
            var end = -1
            var viewText: String
            if (highlight.path == "title") {
                for (text in highlight.texts!!) {
                    if (text.type == "hit") {
                        start = binding.videoTitle.text.toString().indexOf(text.value!!)
                        end = start + text.value!!.length
                    } else continue
                    if (start < 0 || end <= start) continue
                    val spannedString = SpannableString(binding.videoTitle.text)
                    spannedString.setSpan(BackgroundColorSpan(color), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
                    spannedString.setSpan(ForegroundColorSpan((binding.root as MaterialCardView).cardBackgroundColor.defaultColor), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    binding.videoTitle.text = spannedString
                }
            } else if (highlight.path == "description") {
                for (text in highlight.texts!!) {
                    if (text.type == "hit") {
                        start = binding.description.text.toString().indexOf(text.value!!)
                        end = start + text.value!!.length
                    } else continue
                    if (start < 0 || end <= start) continue
                    val spannedString = SpannableString(binding.description.text)
                    spannedString.setSpan(BackgroundColorSpan(Color.YELLOW), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
                    binding.description.text = spannedString
                }
            }
        }

        init {
            binding.root.setOnClickListener { view: View? -> clickListener.onClick(getItem(adapterPosition)) }
        }
    }
}