package vn.linh.androidslidebutton

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ImageView

class SlideToLockButton @SuppressLint("ClickableViewAccessibility")
constructor(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    var dX = 0f
    var minX = 0f
    var maxX = 0f
    var centerX = 0f

    var state: State? = null
        set(value) {
            when (value) {
                State.UNLOCK -> setBackgroundColor(Color.RED)
                State.LOCK -> setBackgroundColor(Color.GREEN)
                State.DISABLE -> setBackgroundColor(Color.GRAY)
            }
        }

    init {
        val imgLock = ImageView(context)
        imgLock.setImageResource(R.drawable.ic_android)
        imgLock.setBackgroundColor(Color.BLUE)
        imgLock.layoutParams = FrameLayout.LayoutParams(240, 240)
        addView(imgLock)
        this.state = State.UNLOCK

        imgLock.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    maxX = (width - v.width).toFloat()
                    centerX = (maxX + minX) / 2
                }

                MotionEvent.ACTION_MOVE -> {
                    move(v, event)
                    changeColor(v)
                    changeAlpha(v)
                }
                MotionEvent.ACTION_UP -> {
                    if (event.rawX > center()) {
                        moveToRight(v)
                    } else {
                        moveToLeft(v)
                    }
                }
                else -> return@OnTouchListener false
            }
            true
        })
    }

    fun center(): Int {
        return (left + right) / 2
    }

    private fun changeColor(v: View) {
        if (v.x < centerX) {
            setBackgroundColor(Color.RED)
        } else {
            setBackgroundColor(Color.GREEN)
        }
    }

    private fun changeAlpha(v: View) {
        var alpha = Math.abs(v.x - centerX) / centerX
        if (alpha < 0.6f) {
            alpha = 0.6f
        }
        this.alpha = alpha
    }

    private fun move(v: View, event: MotionEvent) {
        var targetX = event.rawX + dX
        if (targetX < 0) {
            targetX = 0f
        } else if (targetX > maxX) {
            targetX = maxX
        }
        v.x = targetX
    }

    private fun moveToLeft(v: View) {
        v.animate()
            .x(minX)
            .setDuration(100)
            .start()
        alpha = 1f
    }

    private fun moveToRight(v: View) {
        v.animate()
            .x(maxX)
            .setDuration(100)
            .start()
        alpha = 1f
    }

    enum class State {
        LOCK, UNLOCK, DISABLE
    }
}

