package vn.linh.androidslidebutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slideToUnLockButtonSimple.listener = object:SlideToLockButtonSimple.Listener{
            override fun onLock() {
                Toast.makeText(this@MainActivity, "lock", Toast.LENGTH_SHORT).show()
            }

            override fun onUnLock() {
                Toast.makeText(this@MainActivity, "unLock", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
