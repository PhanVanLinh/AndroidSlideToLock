package vn.linh.androidslidebutton

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

class SlideToLockButtonSimple @SuppressLint("ClickableViewAccessibility")
constructor(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var dX = 0f
    private var minX = 0f
    private var maxX = 0f
    private var centerX = 0f
    var listener: Listener? = null

    init {
        val imgLock = ImageView(context)
        imgLock.setImageResource(R.drawable.ic_android)
        imgLock.setBackgroundColor(Color.BLUE)
        imgLock.layoutParams = FrameLayout.LayoutParams(150, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(imgLock)

        imgLock.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    minX = 0f
                    maxX = (width - v.width).toFloat()
                    centerX = (maxX + minX) / 2
                }

                MotionEvent.ACTION_MOVE -> {
                    moveLock(v, event)
                    changeColor(v)
                }
                MotionEvent.ACTION_UP -> {
                    if (v.x > centerX) {
                        moveLockToRight(v)
                        listener?.onLock()
                    } else {
                        moveLockToLeft(v)
                        listener?.onUnLock()
                    }
                }
                else -> return@OnTouchListener false
            }
            true
        })
        setBackgroundColor(Color.GREEN)
    }


    private fun changeColor(lockView: View) {
        if (lockView.x < centerX) {
            setBackgroundColor(Color.RED)
        } else {
            setBackgroundColor(Color.GREEN)
        }
    }

    private fun moveLock(lockView: View, event: MotionEvent) {
        var targetX = event.rawX + dX
        if (targetX < 0) {
            targetX = 0f
        } else if (targetX > maxX) {
            targetX = maxX
        }
        lockView.x = targetX
    }

    private fun moveLockToLeft(lockView: View) {
        lockView.animate().x(minX).setDuration(100).start()
    }

    private fun moveLockToRight(lockView: View) {
        lockView.animate().x(maxX).setDuration(100).start()
    }

    interface Listener {
        fun onLock()
        fun onUnLock()
    }
}

