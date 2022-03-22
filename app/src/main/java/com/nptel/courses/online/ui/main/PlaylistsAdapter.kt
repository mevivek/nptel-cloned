package com.nptel.courses.online.ui.main

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.databinding.PlaylistBinding
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.OnClickListener
import com.nptel.courses.online.utility.ShimmerViewConverter
import java.util.*

class PlaylistsAdapter(private val listener: OnClickListener<Playlist>) : ListAdapter<Playlist, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return newItem.title == oldItem.title && newItem.description == oldItem.description
    }
}) {
    private lateinit var colors: IntArray
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.id.contains("dummy")) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ViewHolder(PlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)) else ShimmerViewHolder(PlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bind(getItem(position)) else (holder as ShimmerViewHolder).bind(getItem(position))
    }

    override fun submitList(list: List<Playlist?>?) {
        super.submitList(list)
//        if (list != null) colors = ColorUtility.getRandomColors(150, 150, 150, list.size)
    }

    internal inner class ViewHolder(private val binding: PlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist?) {
            binding.playlist = playlist
            val color = colors[layoutPosition]
            binding.title.setTextColor(color)
            binding.description.setTextColor(ColorUtility.getLighterVersion(color, "#B3"))
            binding.videosCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.videosCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
            binding.line.setBackgroundColor(ColorUtility.getLighterVersion(color))
            val root = binding.root as MaterialCardView
            root.setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)))
            root.strokeColor = ColorUtility.getLighterVersion(color)
        }

        init {
            binding.root.setOnClickListener { listener.onClick(getItem(layoutPosition)) }
        }
    }

    internal inner class ShimmerViewHolder(private val binding: PlaylistBinding) : RecyclerView.ViewHolder(ShimmerViewConverter.wrap(binding.root)) {
        fun bind(playlist: Playlist?) {
            binding.playlist = playlist
            val color = colors[layoutPosition]
            val lighterColor = ColorUtility.getLighterVersion(color, "#36")
            binding.moduleImage.setBackgroundColor(lighterColor)
            binding.title.setTextColor(0)
            binding.title.setBackgroundColor(lighterColor)
            binding.description.setTextColor(0)
            binding.description.setBackgroundColor(lighterColor)
            (binding.root as MaterialCardView).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)))
            binding.videosCount.setTextColor(0)
            binding.videosCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
        }
    }

    fun startShimmer() {
        val playlists: MutableList<Playlist?> = ArrayList()
        for (i in 0..9) playlists.add(Playlist.dummy())
        submitList(playlists)
    }

    init {
        startShimmer()
    }
}