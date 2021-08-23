package com.eaapps.schoolsguide.binding


import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.ParseException
import java.text.SimpleDateFormat


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
                },
                start,
                end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            val spanColor = ForegroundColorSpan(spanColor)
            span.setSpan(spanColor, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            textView.text = span
        }
    }

    @JvmStatic
    @BindingAdapter("shortString")
    fun shortString(textView: TextView, txt: String?) {
        val width = 150
        var txtSub = txt
        if (txt != null && txt.length > width) {
            txtSub = txt.substring(0, width - 3) + "..."
        }
        textView.text = txtSub
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter("dateString")
    fun dateString(textView: TextView, txt: String?) {
        txt?.apply {
            textView.apply {
                val simpleDate = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
                try {
                    val date = simpleDate.parse(txt)
                    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val milliseconds =
                        date.time
                    val dateText = DateUtils.getRelativeTimeSpanString(milliseconds, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_RELATIVE)
                    text = dateText
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

        }
    }

    @JvmStatic
    @BindingAdapter("android:text", "spanCheckColor", "checkClick")
    fun checkBoxSpanClick(
        checkBox: MaterialCheckBox,
        text: String,
        spanColor: Int,
        click: () -> Unit
    ) {
        if (text.contains("{")) {
            checkBox.movementMethod = LinkMovementMethod.getInstance()

            val start = text.indexOf("{")
            val end = text.indexOf("}") - 1
            val removePks = text.replace("{", "").replace("}", "")
            val span = SpannableString(removePks)

            span.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        click()
                    }


                },
                start,
                end,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            val spanColor = ForegroundColorSpan(spanColor)
            span.setSpan(spanColor, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            checkBox.text = span
        }
    }


}



