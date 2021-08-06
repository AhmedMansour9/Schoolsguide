package com.eaapps.schoolsguide.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object EditBinding {
    @JvmStatic
    @BindingAdapter("input_Error")
    fun textInputError(textInputEditText: TextInputEditText, msg: String?) {
        if (msg != null && msg.isNotBlank())
            textInputEditText.error = msg
    }

    @JvmStatic
    @BindingAdapter("input_Helper")
    fun textInputHelper(textInputLayout: TextInputLayout, msg: String?) {
        textInputLayout.helperText = msg ?: ""
    }
}