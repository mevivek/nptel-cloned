package com.nptel.courses.online.interfaces

import androidx.annotation.IdRes

interface ListItemClickListener<T> {
    fun onClick(t: T)
    fun onOptionClicked(t: T, @IdRes optionId: Int)
}