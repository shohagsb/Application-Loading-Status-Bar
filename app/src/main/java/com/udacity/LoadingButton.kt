package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var circleRadius = 360f
    private var buttonWidth = 0f;
    val animDuration = 3000L

    private var buttonValueAnimator = ValueAnimator()
    private var circleValueAnimator: ValueAnimator? = null


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

        //Draw the Animated Rectangle
        paint.color = context.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(
            0f,
            0f,
            buttonWidth,
            heightSize.toFloat(),
            paint
        )

        paint.color = Color.WHITE
        // Draw text.
        canvas.drawText(
            context.getString(R.string.button_name),
            widthSize / 2f, heightSize / 2f + 18, paint
        )

        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas.drawArc(
            widthSize - 345f, heightSize / 2 - 40f,
            widthSize - 270f, heightSize / 2 + 40f,
            0f, circleRadius, true, paint
        )

//        canvas.drawArc(
//            widthSize/ 1.4f, heightSize / 1.95f ,
//            widthSize/1.55f, heightSize / 2.10f,
//            0f, circleRadius, true, paint
//        )
        canvas.restore()
    }

//    circleAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
//        val animDuration
//        duration = animDuration
//        repeatMode = ValueAnimator.REVERSE
//        repeatCount = ValueAnimator.INFINITE
//        interpolator = AccelerateInterpolator(1f)
//        addUpdateListener {
//            loadingAngle = animatedValue as Float
//            invalidate()
//        }
//    }

//    fun showLoading() {
//
//    }

    fun showLoading() {
        circleValueAnimator = ValueAnimator.ofFloat(0F, circleRadius).apply {
            duration = animDuration
            //interpolator = AccelerateDecelerateInterpolator()
            interpolator = AccelerateInterpolator(1f)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener { animation ->
                circleRadius = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
        buttonValueAnimator = ValueAnimator.ofFloat(0F, widthSize.toFloat()).apply {
            duration = animDuration
            interpolator = AccelerateInterpolator(1f)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                buttonWidth = animatedValue as Float
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