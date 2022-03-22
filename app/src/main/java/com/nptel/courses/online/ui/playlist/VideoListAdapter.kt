package com.nptel.courses.online.ui.playlist

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.R
import com.nptel.courses.online.databinding.VideoListItemBinding
import com.nptel.courses.online.entities.Video
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.ShimmerViewConverter
import java.util.*

class VideoListAdapter(clickListener: ListItemClickListener<Video>) : ListAdapter<Video, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.youtubeVideoId == oldItem.youtubeVideoId
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.title == oldItem.title && newItem.description == oldItem.description
    }
}) {
    private var currentPlayingItem: VideoListItemBinding? = null
    private var playingVideoPosition: Int
    private var showDelete = false
    private lateinit var colors: IntArray
    private var clickListener: ListItemClickListener<Video>
    override fun submitList(list: List<Video?>?) {
        super.submitList(list)
//        if (list != null) colors = ColorUtility.getRandomColors(150, 150, 150, list.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.id.contains("dummy")) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) else ShimmerViewHolder(VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bind(getItem(position)) else (holder as ShimmerViewHolder).bind(getItem(position))
    }

    fun setShowDelete() {
        showDelete = true
    }

    fun startShimmer() {
        val videos: MutableList<Video?> = ArrayList()
        for (i in 0..9) videos.add(Video.dummy())
        submitList(videos)
    }

    internal inner class ViewHolder(private val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video?) {
            binding.video = video
            binding.showDelete = showDelete
            if (playingVideoPosition == adapterPosition) {
                binding.playing.visibility = View.VISIBLE
                (binding.root as MaterialCardView).strokeColor = itemView.context.resources.getColor(R.color.youtube_red_color)
                (binding.root as MaterialCardView).cardElevation = 10f
            } else {
                binding.playing.visibility = View.GONE
                val color = getColor(adapterPosition)
                binding.line.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"))
                binding.videoTitle.setTextColor(color)
                binding.videoDuration.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
                binding.videoDuration.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
                binding.coursePlaylist.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
                binding.line2.setBackgroundColor(ColorUtility.getLighterVersion(color, "#1A"))
                binding.favourite.imageTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC"))
                binding.addToPlaylist.imageTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC"))
                binding.shareVideo.imageTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#CC"))
                val root = binding.root as MaterialCardView
                root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color))))
                root.strokeColor = ColorUtility.getLighterVersion(color)
                root.cardElevation = 0f
            }
        }

        private fun getColor(position: Int): Int {
            if (colors.size > position) return colors[position]
//            colors = ColorUtility.getRandomColors(150, 150, 150, currentList.size, colors)
            return colors.get(position)
        }

        private fun setCurrentPlaying() {
            if (currentPlayingItem != null) {
                currentPlayingItem?.playing?.visibility = View.GONE
                (currentPlayingItem?.root as MaterialCardView).strokeColor = itemView.context.resources.getColor(R.color.mildBackgroundStrokeColor)
                (currentPlayingItem?.root as MaterialCardView).cardElevation = 0f
            }
            binding.playing.visibility = View.VISIBLE
            (binding.root as MaterialCardView).strokeColor = itemView.context.resources.getColor(R.color.youtube_red_color)
            (binding.root as MaterialCardView).cardElevation = 10f
        }

        init {
            binding.root.setOnClickListener {
                setCurrentPlaying()
                currentPlayingItem = binding
                playingVideoPosition = layoutPosition
                clickListener.onClick(getItem(playingVideoPosition))
            }
            binding.shareVideo.setOnClickListener { view: View -> clickListener.onOptionClicked(getItem(layoutPosition), view.id) }
            binding.favourite.setOnClickListener { view: View -> clickListener.onOptionClicked(getItem(layoutPosition), view.id) }
            binding.addToPlaylist.setOnClickListener { view: View -> clickListener.onOptionClicked(getItem(layoutPosition), view.id) }
            binding.deleteVideo.setOnClickListener { view: View -> clickListener.onOptionClicked(getItem(layoutPosition), view.id) }
        }
    }

    internal inner class ShimmerViewHolder(private val binding: VideoListItemBinding) : RecyclerView.ViewHolder(ShimmerViewConverter.wrap(binding.root)) {
        fun bind(video: Video?) {
            binding.video = video
            binding.executePendingBindings()
            val color = colors[layoutPosition]
            val lighterColor = ColorUtility.getLighterVersion(color, "#36")
            binding.videoThumbnail.setBackgroundColor(lighterColor)
            binding.videoTitle.setTextColor(0)
            binding.videoTitle.setBackgroundColor(lighterColor)

            /*binding.description.setTextColor(0);
            binding.description.setBackgroundColor(lighterColor);*/(binding.root as MaterialCardView).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)))
            binding.videoDuration.setTextColor(0)
            binding.videoDuration.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
            binding.favourite.setImageDrawable(null)
            binding.favourite.backgroundTintList = ColorStateList.valueOf(lighterColor)
            binding.addToPlaylist.setImageDrawable(null)
            binding.addToPlaylist.backgroundTintList = ColorStateList.valueOf(lighterColor)
            binding.shareVideo.setImageDrawable(null)
            binding.shareVideo.backgroundTintList = ColorStateList.valueOf(lighterColor)
            binding.deleteVideo.setImageDrawable(null)
            binding.deleteVideo.backgroundTintList = ColorStateList.valueOf(lighterColor)
        }
    }

    init {
        playingVideoPosition = -1
        this.clickListener = clickListener
        startShimmer()
    }
}