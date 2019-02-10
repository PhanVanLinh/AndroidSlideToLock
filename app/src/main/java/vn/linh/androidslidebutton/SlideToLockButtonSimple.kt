package vn.linh.androidslidebutton

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_lock.view.*

class SlideToLockButtonSimple @SuppressLint("ClickableViewAccessibility")
constructor(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var dX = 0f
    private var minX = 0f
    private var maxX = 0f
    private var centerX = 0f
    var listener: Listener? = null
    var state: State = State.UNDEFINE
        set(value) {
            changeState(value)
            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_lock, this, true)
        val imgLock = findViewById<ImageView>(R.id.image_lock)
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

    private fun changeState(state: State) {
        progress_loading.visibility = View.GONE
        text_time.visibility = View.GONE

        if (state == State.LOADING) {
            progress_loading.visibility = View.VISIBLE
        }
        if (state == State.UNLOCK) {
            moveLockToLeft(image_lock)
            changeColor(image_lock)
        }
        if (state == State.LOCK) {
            moveLockToRight(image_lock)
            changeColor(image_lock)
        }
        if (state == State.READY_TO_LOCK) {
            text_time.text = "100"
            text_time.visibility = View.VISIBLE
        }
    }

    fun setProgress(time: String) {
        text_time.text = time
    }

    interface Listener {
        fun onLock()
        fun onUnLock()
    }

    enum class State {
        UNLOCK, LOADING, READY_TO_LOCK, LOCK, UNDEFINE
    }
}

