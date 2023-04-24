package com.example.em_tuntiesimerkki1

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

class LatestDataView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    // Max row count list to show
    val maxRows = 5

    init {
        this.orientation = VERTICAL

        var someTextView : TextView = TextView(context)
        someTextView.measure(0,0)

        var rowHight = someTextView.measuredHeight

        this.measure(0,0)

        var additionalHeight = this.measuredHeight + maxRows * rowHight
        this.minimumHeight = additionalHeight

    }

    fun addData(message :String) {

        // Remove oldest rows while possible
        while (this.childCount >= maxRows) {
            this.removeViewAt(0)
        }

        var newTextView : TextView = TextView(context) as TextView
        newTextView.text = message
        newTextView.setBackgroundColor(Color.BLACK)
        newTextView.setTextColor(Color.YELLOW)
        this.addView(newTextView)

        val fadeAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.customfade)
        newTextView.startAnimation(fadeAnimation)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}