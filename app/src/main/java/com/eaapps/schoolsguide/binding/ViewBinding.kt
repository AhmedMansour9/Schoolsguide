package com.eaapps.schoolsguide.binding


import android.annotation.SuppressLint
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateUtils
import android.text.format.DateUtils.getRelativeTimeSpanString
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
    @BindingAdapter("shortString", "maxLength")
    fun shortString(textView: TextView, txt: String?, maxLength: Int?) {
        val width = maxLength ?: 120
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
                    /*
                    FORMAT_SHOW_YEAR = August 17 ,2021
                    FORMAT_ABBREV_MONTH = August 17
                    formatDateTime(context,milliseconds,flag)
                     */
                    val dateText = getRelativeTimeSpanString(
                        milliseconds,
                        System.currentTimeMillis(),
                        0L,
                        DateUtils.FORMAT_ABBREV_RELATIVE
                    )
                    text = dateText
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

        }
    }

    @JvmStatic
    @BindingAdapter("htmlString")
    fun htmlString(textView: TextView, txt: String?) {
        txt?.apply {
            textView.text = Html.fromHtml(txt).trim()
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



