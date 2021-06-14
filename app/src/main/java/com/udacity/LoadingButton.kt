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

    private var circleRadius = 0f
    private var buttonWidth = 0f
    private val animDuration = 3000L

    private var buttonText = context.getString(R.string.button_name)

    private var buttonValueAnimator = ValueAnimator()
    private var circleValueAnimator = ValueAnimator()


    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                cancelAnimation()
                resetLoadingValue()
                buttonText = context.getString(R.string.button_loading)
                startAnimation()
            }
            ButtonState.Completed -> {
                buttonText = context.getString(R.string.button_name)
                cancelAnimation()
                resetLoadingValue()
            }
            else -> {
                buttonText = context.getString(R.string.button_name)
            }
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        textSize = resources.getDimension(R.dimen.default_text_size)
    }


    init {
        isClickable = true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTranslatedTextExample(canvas)

    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
        canvas.save()
        //Button Background
        canvas.drawColor(ContextCompat.getColor(context, R.color.colorPrimary))
        //Draw the Rectangle
        paint.color = context.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(
            0f,
            0f,
            buttonWidth,
            heightSize.toFloat(),
            paint
        )

        // Draw text.
        paint.color = Color.WHITE
        canvas.drawText(
            buttonText,
            widthSize / 2f, heightSize / 2f + 18, paint
        )

        // Draw Circle
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        canvas.drawArc(
            widthSize - 245f, heightSize / 2 - 40f,
            widthSize - 170f, heightSize / 2 + 40f,
            0f, circleRadius, true, paint
        )

//        canvas.drawArc(
//            widthSize/ 1.4f, heightSize / 1.95f ,
//            widthSize/1.55f, heightSize / 2.10f,
//            0f, circleRadius, true, paint
//        )
        canvas.restore()
    }

    private fun startAnimation() {
        circleValueAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
            duration = animDuration
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
            addUpdateListener { animation ->
                buttonWidth = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun cancelAnimation() {
        circleValueAnimator.cancel()
        buttonValueAnimator.cancel()
    }

    private fun resetLoadingValue() {
        circleRadius = 0f
        buttonWidth = 0f
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