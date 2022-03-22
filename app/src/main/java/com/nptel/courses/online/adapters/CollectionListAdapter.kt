package com.nptel.courses.online.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.adapters.CollectionListAdapter.PlaylistViewHolder
import com.nptel.courses.online.databinding.CollectionBinding
import com.nptel.courses.online.entities.Collection
import com.nptel.courses.online.interfaces.ListItemClickListener
import com.nptel.courses.online.utility.ColorUtility

class CollectionListAdapter(private val clickListener: ListItemClickListener<Collection>) : ListAdapter<Collection, PlaylistViewHolder>(object : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.name == newItem.name
    }
}) {
    private lateinit var colors: IntArray
    override fun submitList(list: List<Collection>?) {
        super.submitList(list)
//        if (list != null) colors = ColorUtility.getRandomColors(150, list.size, false).toIntArray()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(CollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaylistViewHolder(private val binding: CollectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collection: Collection?) {
            binding.collection = collection
            val color = colors[layoutPosition]
            binding.name.setTextColor(color)
            binding.videoCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.edit.imageTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#80"))
            val root = binding.root as MaterialCardView
            root.setCardBackgroundColor(ColorUtility.getLighterVersion(color))
            root.strokeColor = ColorUtility.getLighterVersion(color, "#33")
        }

        init {
            binding.root.setOnClickListener { clickListener.onClick(getItem(layoutPosition)) }
            binding.edit.setOnClickListener { clickListener.onOptionClicked(getItem(layoutPosition), it.id) }
        }
    }
}