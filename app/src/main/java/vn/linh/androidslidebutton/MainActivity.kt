package vn.linh.androidslidebutton

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var readyToLockTimer = object : CountDownTimer(3000, 1000) {
        override fun onFinish() {
            slide_to_lock.state = SlideToLockButtonSimple.State.LOCK
        }

        override fun onTick(millisUntilFinished: Long) {
            slide_to_lock.setProgress("" + millisUntilFinished)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slide_to_lock.state = SlideToLockButtonSimple.State.UNLOCK

        slide_to_lock.listener = object : SlideToLockButtonSimple.Listener {
            override fun onLock() {
                Toast.makeText(this@MainActivity, "lock", Toast.LENGTH_SHORT).show()

                slide_to_lock.state = SlideToLockButtonSimple.State.LOADING
                Handler().postDelayed({
                    slide_to_lock.state = SlideToLockButtonSimple.State.READY_TO_LOCK
                    readyToLockTimer.start()

                }, 3000)
            }

            override fun onUnLock() {
                Toast.makeText(this@MainActivity, "unLock", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
