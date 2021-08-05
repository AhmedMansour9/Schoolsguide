package com.eaapps.schoolsguide.binding


import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter


object ViewBinding {

    @JvmStatic
    @BindingAdapter("visible")
    fun viewVisible(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun viewGone(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:text", "spanColor", "click")
    fun textViewSpanClick(textView: TextView, text: String, spanColor: Int, click: () -> Unit) {
        if (text.contains("{")) {
            textView.movementMethod = LinkMovementMethod.getInstance()

            val start = text.indexOf("{")
            val end = text.indexOf("}") - 1
            val removePks = text.replace("{", "").replace("}", "")
            val span = SpannableString(removePks)

            span.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        click()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                    }
                },
                start,
                end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            val spanColor = ForegroundColorSpan(spanColor)
            span.setSpan(spanColor,start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.text = span
        }
    }


}



