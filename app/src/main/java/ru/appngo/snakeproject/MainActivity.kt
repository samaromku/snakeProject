package ru.appngo.snakeproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = LinearLayout.LayoutParams(100, 100)
        head.background = ContextCompat.getDrawable(this, R.drawable.circle)


        Thread(Runnable {
            while (true) {
                Thread.sleep(500)
                runOnUiThread{
                    (head.layoutParams as LinearLayout.LayoutParams).topMargin += 100
                    container.removeView(head)
                    container.addView(head)
                }
            }
        }).start()
    }
}
