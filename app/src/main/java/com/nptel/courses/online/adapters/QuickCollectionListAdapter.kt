package com.nptel.courses.online.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.R
import com.nptel.courses.online.databinding.QuickCollectionItemBinding
import com.nptel.courses.online.entities.Collection
import com.nptel.courses.online.utility.ColorUtility

class QuickCollectionListAdapter(private val videoId: String) : ListAdapter<Collection, QuickCollectionListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.name == newItem.name
    }
}) {
    private var selectedCard: MaterialCardView? = null
    var selectedCollection: Collection? = null
    private lateinit var colors: IntArray
    override fun submitList(list: List<Collection?>?) {
        super.submitList(list)
//        if (list != null) colors = ColorUtility.getRandomColors(150, 150, 150, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(QuickCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: QuickCollectionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collection: Collection) {
            binding.alreadyAdded.visibility = if (collection.videos?.contains(videoId) == true) View.VISIBLE else View.GONE
            binding.collection = collection
            val color = colors[adapterPosition]
            binding.name.setTextColor(color)
            binding.videoCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.alreadyAdded.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color))
            binding.alreadyAdded.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            val root = binding.root as MaterialCardView
            root.setCardBackgroundColor(ColorUtility.ARGB_To_RGB(ColorUtility.getLighterVersion(color)))
            binding.selected.visibility = if (selectedCollection != null && selectedCollection == collection) View.VISIBLE else View.GONE
            root.cardElevation = if (selectedCollection != null && selectedCollection == collection) 10.toFloat() else 0.toFloat()
        }

        init {
            binding.root.setOnClickListener { view: View? ->
                if (getItem(layoutPosition).videos?.contains(videoId) == true) {
                    Toast.makeText(binding.root.context, "This video is already in this playlist.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                selectedCard?.cardElevation = 0f
                selectedCard?.findViewById<View>(R.id.selected)?.visibility = View.GONE
                selectedCard = view as MaterialCardView?
                selectedCard!!.cardElevation = 10f
                selectedCard!!.findViewById<View>(R.id.selected).visibility = View.VISIBLE
                selectedCollection = getItem(adapterPosition)
            }
        }
    }
}