package com.nptel.courses.online.ui.main

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nptel.courses.online.databinding.CourseItemBinding
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.OnClickListener
import com.nptel.courses.online.utility.ShimmerViewConverter
import java.util.*

class CourseListAdapter(private val listener: OnClickListener<Course>) : ListAdapter<Course, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return newItem.title == oldItem.title
    }
}) {
    private lateinit var colors: IntArray
    override fun submitList(list: List<Course>?) {
        super.submitList(list)
//        if (list != null) colors = ColorUtility.getRandomColors(150, 150, 150, list.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)!!.id.contains("dummy")) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) ViewHolder(CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) else ShimmerViewHolder(CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) holder.bind(getItem(position)) else (holder as ShimmerViewHolder).bind(getItem(position))
    }

    internal inner class ViewHolder(private val binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(course: Course?) {
            binding.course = course
            val color = colors[adapterPosition]
            binding.initial.setTextColor(color)
            val lighterColor = ColorUtility.getLighterVersion(color, "#36")
            binding.initial.backgroundTintList = ColorStateList.valueOf(lighterColor)
            binding.name.setTextColor(color)
            (binding.root as MaterialCardView).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)))
            binding.playlistsCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.playlistsCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
            binding.videosCount.setTextColor(ColorUtility.getLighterVersion(color, "#80"))
            binding.videosCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
        }

        init {
            binding.root.setOnClickListener { view: View? -> listener.onClick(getItem(adapterPosition)) }
        }
    }

    internal inner class ShimmerViewHolder(private val binding: CourseItemBinding) : RecyclerView.ViewHolder(ShimmerViewConverter.wrap(binding.root)) {
        fun bind(course: Course?) {
            binding.course = course
            val color = colors[adapterPosition]
            binding.initial.setTextColor(0)
            val lighterColor = ColorUtility.getLighterVersion(color, "#36")
            binding.initial.backgroundTintList = ColorStateList.valueOf(lighterColor)
            binding.name.setTextColor(0)
            binding.name.setBackgroundColor(lighterColor)
            (binding.root as MaterialCardView).setCardBackgroundColor(ColorUtility.getLighterVersion(ColorUtility.getLighterVersion(color)))
            binding.playlistsCount.setTextColor(0)
            binding.playlistsCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
            binding.videosCount.setTextColor(0)
            binding.videosCountContainer.backgroundTintList = ColorStateList.valueOf(ColorUtility.getLighterVersion(color, "#1A"))
        }
    }

    fun startShimmer() {
        val courses: MutableList<Course> = ArrayList()
        for (i in 0..9) courses.add(Course.dummy())
        submitList(courses)
    }

    init {
        startShimmer()
    }
}