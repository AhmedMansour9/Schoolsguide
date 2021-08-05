package com.eaapps.schoolsguide.binding


import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

object ProgressBinding {

    @BindingAdapter("android:progress")
    @JvmStatic
    fun progressBar(progressBar: ProgressBar, progress: Long) {
        progressBar.progress = progress.toInt()
    }

}