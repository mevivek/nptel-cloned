package com.nptel.courses.online.utility

import android.view.View
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.nptel.courses.online.R

object ShimmerViewConverter {
    fun wrap(view: View): View {
        val shimmerFrameLayout = ShimmerFrameLayout(view.context)
        val layoutParams = ViewGroup.LayoutParams(view.layoutParams)
        shimmerFrameLayout.layoutParams = layoutParams
        shimmerFrameLayout.addView(view)
        shimmerFrameLayout.startShimmer()
        return shimmerFrameLayout
    }

    fun convert(view: View): View {
        val shimmerFrameLayout = ShimmerFrameLayout(view.context)
        val layoutParams = ViewGroup.LayoutParams(view.layoutParams)
        shimmerFrameLayout.layoutParams = layoutParams
        if (view is ViewGroup) setBackground(view) else view.setBackgroundColor(view.context.resources.getColor(R.color.mildBackgroundColor))
        shimmerFrameLayout.addView(view)
        shimmerFrameLayout.startShimmer()
        return shimmerFrameLayout
    }

    fun setBackground(view: ViewGroup) {
        for (i in 0 until view.childCount) if (view.getChildAt(i) is ViewGroup) setBackground(view.getChildAt(i) as ViewGroup) else view.getChildAt(i).setBackgroundColor(view.context.resources.getColor(R.color.mildBackgroundColor))
    }
}