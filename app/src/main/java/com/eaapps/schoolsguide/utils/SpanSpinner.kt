package com.eaapps.schoolsguide.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class SpanSpinner(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {
    init {
        initView()

    }




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        initView()
    }

    fun initView() {
        val textView = TextView(context)
        val set = ConstraintSet()
        textView.id = View.generateViewId()
        textView.text = "Eslam"
         textView.setBackgroundColor(Color.RED)
        addView(textView,0)
        set.clone(this)
        set.connect(textView.id,ConstraintSet.TOP,this.id,ConstraintSet.TOP,60)
        set.connect(textView.id,ConstraintSet.START,this.id,ConstraintSet.START,60)
        set.connect(textView.id,ConstraintSet.END,this.id,ConstraintSet.END,60)
        set.applyTo(this)
//        val layoutParams = layoutParams as LayoutParams
//        layoutParams.topToTop = LayoutParams.PARENT_ID
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//        textView.layoutParams = layoutParams
//        textView.text = "Eslam"
//        addView(textView)

    }
}