package ru.appngo.snakeproject

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.appngo.snakeproject.SnakeCore.isPlay
import ru.appngo.snakeproject.SnakeCore.startTheGame

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = LinearLayout.LayoutParams(100, 100)
        head.background = ContextCompat.getDrawable(this, R.drawable.circle)
        startTheGame()
        SnakeCore.nextMove = { move(Directions.BOTTOM, head) }

        ivArrowUp.setOnClickListener { SnakeCore.nextMove = { move(Directions.UP, head) } }
        ivArrowBottom.setOnClickListener { SnakeCore.nextMove = { move(Directions.BOTTOM, head) } }
        ivArrowLeft.setOnClickListener { SnakeCore.nextMove = { move(Directions.LEFT, head) } }
        ivArrowRight.setOnClickListener { SnakeCore.nextMove = { move(Directions.RIGHT, head) } }
        ivPause.setOnClickListener {
            if (isPlay) {
                ivPause.setImageResource(R.drawable.ic_play)
            } else {
                ivPause.setImageResource(R.drawable.ic_pause)
            }
            SnakeCore.isPlay = !isPlay
        }
    }

    fun move(directions: Directions, head: View) {
        when (directions) {
            Directions.UP -> (head.layoutParams as LinearLayout.LayoutParams).topMargin -= 100
            Directions.BOTTOM -> (head.layoutParams as LinearLayout.LayoutParams).topMargin += 100
            Directions.LEFT -> (head.layoutParams as LinearLayout.LayoutParams).leftMargin -= 100
            Directions.RIGHT -> (head.layoutParams as LinearLayout.LayoutParams).leftMargin += 100
        }
        runOnUiThread {
            container.removeView(head)
            container.addView(head)
        }

    }
}

enum class Directions {
    UP,
    RIGHT,
    BOTTOM,
    LEFT
}
