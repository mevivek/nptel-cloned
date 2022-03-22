package com.nptel.courses.online.utility

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object CustomBindingAdapter {
    @JvmStatic
    @BindingAdapter("app:url")
    fun setUrl(imageView: ImageView?, url: String?) {
        Picasso.get().load(url).into(imageView)
    }

    @BindingAdapter("app:srcCompat")
    fun setSrcCompat(imageView: AppCompatImageView, drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }
}