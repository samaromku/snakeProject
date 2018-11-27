package ru.appngo.snakeproject

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import ru.appngo.snakeproject.SnakeCore.isPlay
import ru.appngo.snakeproject.SnakeCore.startTheGame

const val HEAD_SIZE = 100

class MainActivity : AppCompatActivity() {
    private val human by lazy {
        ImageView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
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

    private fun generateNewHuman() {
        Thread(Runnable {
            human.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
            human.setImageResource(R.drawable.ic_person)
            (human.layoutParams as FrameLayout.LayoutParams).topMargin = (1..10).random() * HEAD_SIZE
            (human.layoutParams as FrameLayout.LayoutParams).leftMargin = (1..10).random() * HEAD_SIZE
            runOnUiThread {
                container.addView(human)
            }
        }).start()
    }

    private fun checkIfSnakeEatsPerson(head: View) {
        if (head.left == human.left && head.top == human.top) {
            container.removeView(human)
            generateNewHuman()
        }
    }

    fun move(directions: Directions, head: View) {
        when (directions) {
            Directions.UP -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -= HEAD_SIZE
            Directions.BOTTOM -> (head.layoutParams as FrameLayout.LayoutParams).topMargin += HEAD_SIZE
            Directions.LEFT -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -= HEAD_SIZE
            Directions.RIGHT -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin += HEAD_SIZE
        }
        runOnUiThread {
            checkIfSnakeEatsPerson(head)
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
