package com.eaapps.schoolsguide.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

object EditBinding {
    @JvmStatic
    @BindingAdapter("input_Error")
    fun textInputError(textInputEditText: TextInputEditText, msg: String?) {
        if (msg != null && msg.isNotBlank())
            textInputEditText.error = msg
    }
}