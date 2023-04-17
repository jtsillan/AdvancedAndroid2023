package com.example.em_tuntiesimerkki1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CustomTemperatureView  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnClickListener {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var temperature = 0

    init
    {
        // Define the colors
        // Collect here what is drawn ones
        paint.color = Color.BLUE
        textPaint.color = Color.WHITE
        textPaint.textSize = 80f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.DEFAULT_BOLD
    }

    // Function to change active temperature inside fragment
    fun changeTemperature(temp :Int) {
        temperature = temp

        if (temperature > 0) {
            paint.color = Color.RED
        }
        else {
            paint.color = Color.BLUE
        }

        // Value changed, draw layout again
        invalidate()
        requestLayout()
    }


    // drawing is typically done in the custom view's onDraw-method
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // you can do all the drawing through the canvas-object
        // parameters: x-coordinate, y-coordinate, size, color
        canvas.drawCircle(width.toFloat() / 2, width.toFloat() / 2, width.toFloat() / 2, paint)

        // parameters: content, x, y, color
        canvas.drawText("${temperature}℃", width.toFloat() / 2, width.toFloat() / 2 + 25, textPaint);
    }

    // If layout has no
    private var size = 200

    // Important component!!
    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        var w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        // if no exact size given (either dp or match_parent)
        // use this one instead as default (wrap_content)
        if (w == 0)
        {
            w = size * 2
        }

        // Whatever the width ends up being, ask for a height that would let the view
        // get as big as it can
        // val minh: Int = View.MeasureSpec.getSize(w) + paddingBottom + paddingTop
        // in this case, we use the height the same as our width, since it's a circle
        val h: Int = View.resolveSizeAndState(
            View.MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )

        setMeasuredDimension(w, h)
    }

    override fun onClick(v: View?) {
    }

}
