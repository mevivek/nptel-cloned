package com.nptel.courses.online.utility;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class CustomBindingAdapter {

    @BindingAdapter("app:url")
    public static void setUrl(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    @BindingAdapter("app:srcCompat")
    public static void setSrcCompat(AppCompatImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }
}
