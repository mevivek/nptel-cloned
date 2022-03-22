package com.nptel.courses.online.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.nptel.courses.online.entities.Video

class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.youtubeVideoId == oldItem.youtubeVideoId
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return newItem.title == oldItem.title && newItem.description == oldItem.description
    }
}