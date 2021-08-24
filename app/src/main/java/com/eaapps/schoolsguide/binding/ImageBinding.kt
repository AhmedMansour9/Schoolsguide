package com.eaapps.schoolsguide.binding


import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.eaapps.schoolsguide.R


object ImageBinding {

    @BindingAdapter("android:src")
    @JvmStatic
    fun imageViewRes(imageView: ImageView, res: Int) {
        imageView.setImageResource(res)
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun imageViewDrawable(imageView: ImageView, drawable: Drawable?) {
        drawable?.apply {
            imageView.setImageDrawable(drawable)
        }
    }

    @BindingAdapter("app:tint")
    @JvmStatic
    fun imageTint(imageView: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color))
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun imageByUrl(imageView: ImageView, url: String?) {
        url?.apply {
            if (this.isNotBlank())
                Glide.with(imageView).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.progress_animation).into(imageView)
        }

    }

    @BindingAdapter("android:background")
    @JvmStatic
    fun imageBackgroundByUrl(imageView: ImageView, url: String?) {
        url?.apply {
            if (this.isNotBlank())
                Glide.with(imageView).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.progress_animation)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            imageView.background = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
        }

    }
}