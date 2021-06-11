package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var circleRadius = 40f

    //private val valueAnimator = ValueAnimator()
    private var valueAnimator: ValueAnimator? = null


    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        //typeface = Typeface.create( "", Typeface.BOLD)
        textSize = resources.getDimension(R.dimen.default_text_size)
    }


    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //canvas.drawColor(resources.getColor(R.color.colorPrimary))
        drawTranslatedTextExample(canvas)

    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = Color.WHITE
        // Draw text.
        canvas.drawText(
            context.getString(R.string.button_name),
            widthSize / 2f, heightSize / 2f + 18, paint
        )

        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas.drawCircle(widthSize / 1.4f, heightSize / 2f, circleRadius, paint)
        canvas.restore()
    }


    fun showLoading() {
        //isVisible = true
        valueAnimator = ValueAnimator.ofFloat(10F, circleRadius).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                circleRadius = animation.animatedValue as Float
                animation.repeatCount = ValueAnimator.INFINITE
                animation.repeatMode = ValueAnimator.REVERSE
                invalidate()
            }
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}