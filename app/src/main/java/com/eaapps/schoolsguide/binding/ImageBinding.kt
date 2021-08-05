package com.eaapps.schoolsguide.binding


import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


object ImageBinding {

    @BindingAdapter("android:src")
    @JvmStatic
    fun imageViewRes(imageView: ImageView, res: Int) {
        imageView.setImageResource(res)
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun imageViewDrawable(imageView: ImageView, drawable: Drawable) {
        imageView.setImageDrawable(drawable)
    }


    @BindingAdapter("app:tint")
    @JvmStatic
    fun imageTint(imageView: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color))
    }

    @BindingAdapter("url")
    @JvmStatic
    fun imageByUrl(imageView: ImageView, url: String) {
        if (url.isNotBlank())
            Glide.with(imageView).load(url).into(imageView)
    }
}