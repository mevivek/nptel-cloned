package com.nptel.courses.online.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.nptel.courses.online.entities.Playlist

class ModuleDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return newItem.title == oldItem.title && newItem.description == oldItem.description
    }
}