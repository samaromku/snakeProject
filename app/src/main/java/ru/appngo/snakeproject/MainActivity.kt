package ru.appngo.snakeproject

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.appngo.snakeproject.SnakeCore.isPlay
import ru.appngo.snakeproject.SnakeCore.startTheGame

const val HEAD_SIZE = 100
const val CELLS_ON_FIELD = 10

class MainActivity : AppCompatActivity() {

    private val allTale = mutableListOf<PartOfTale>()
    private var currentDirection: Directions = Directions.BOTTOM
    private val human by lazy {
        ImageView(this)
    }
    private val head by lazy {
        ImageView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        head.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        head.setImageResource(R.drawable.snake_head)
        container.layoutParams = LinearLayout.LayoutParams(HEAD_SIZE * CELLS_ON_FIELD, HEAD_SIZE * CELLS_ON_FIELD)

        startTheGame()
        generateNewHuman()
        SnakeCore.nextMove = { move(Directions.BOTTOM) }

        ivArrowUp.setOnClickListener {
            SnakeCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.UP, Directions.BOTTOM) }
        }
        ivArrowBottom.setOnClickListener {
            SnakeCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.BOTTOM, Directions.UP) }
        }
        ivArrowLeft.setOnClickListener {
            SnakeCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.LEFT, Directions.RIGHT) }
        }
        ivArrowRight.setOnClickListener {
            SnakeCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.RIGHT, Directions.LEFT) }
        }
        ivPause.setOnClickListener {
            if (isPlay) {
                ivPause.setImageResource(R.drawable.ic_play)
            } else {
                ivPause.setImageResource(R.drawable.ic_pause)
            }
            SnakeCore.isPlay = !isPlay
        }
    }

    private fun checkIfCurrentDirectionIsNotOpposite(rightDirection: Directions, oppositeDirection: Directions) {
        if (currentDirection == oppositeDirection) {
            move(currentDirection)
        } else {
            move(rightDirection)
        }
    }

    private fun generateNewHuman() {
        human.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        human.setImageResource(R.drawable.ic_person)
        (human.layoutParams as FrameLayout.LayoutParams).topMargin = (0 until CELLS_ON_FIELD).random() * HEAD_SIZE
        (human.layoutParams as FrameLayout.LayoutParams).leftMargin = (0 until CELLS_ON_FIELD).random() * HEAD_SIZE
        container.removeView(human)
        container.addView(human)
    }

    private fun checkIfSnakeEatsPerson() {
        if (head.left == human.left && head.top == human.top) {
            generateNewHuman()
            addPartOfTale(head.top, head.left)
        }
    }

    private fun addPartOfTale(top: Int, left: Int) {
        val talePart = drawPartOfTale(top, left)
        allTale.add(PartOfTale(top, left, talePart))
    }

    private fun drawPartOfTale(top: Int, left: Int): ImageView {
        val taleImage = ImageView(this)
        taleImage.setImageResource(R.drawable.snake_scales)
        taleImage.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        (taleImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (taleImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left

        container.addView(taleImage)
        return taleImage
    }

    fun move(direction: Directions) {
        when (direction) {
            Directions.UP -> {
                moveHeadAndRotate(Directions.UP, 90f, -HEAD_SIZE)
            }
            Directions.BOTTOM -> {
                moveHeadAndRotate(Directions.BOTTOM, 270f, HEAD_SIZE)
            }
            Directions.LEFT -> {
                moveHeadAndRotate(Directions.LEFT, 0f, -HEAD_SIZE)
            }
            Directions.RIGHT -> {
                moveHeadAndRotate(Directions.RIGHT, 180f, HEAD_SIZE)
            }
        }
        runOnUiThread {
            if (checkIfSnakeSmash()) {
                isPlay = false
                showScore()
                return@runOnUiThread
            }
            makeTaleMove()
            checkIfSnakeEatsPerson()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun moveHeadAndRotate(direction: Directions, angle: Float, coordinates: Int) {
        head.rotation = angle
        when (direction) {
            Directions.UP, Directions.BOTTOM -> {
                (head.layoutParams as FrameLayout.LayoutParams).topMargin += coordinates
            }
            Directions.LEFT, Directions.RIGHT -> {
                (head.layoutParams as FrameLayout.LayoutParams).leftMargin += coordinates
            }
        }
        currentDirection = direction
    }

    private fun showScore() {
        AlertDialog.Builder(this)
            .setTitle("Your score: ${allTale.size} items")
            .setPositiveButton("ok") { _, _ ->
                this.recreate()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun checkIfSnakeSmash(): Boolean {
        for (talePart in allTale) {
            if (talePart.left == head.left && talePart.top == head.top) {
                return true
            }
        }
        if (head.top < 0
            || head.left < 0
            || head.top >= HEAD_SIZE * CELLS_ON_FIELD
            || head.left >= HEAD_SIZE * CELLS_ON_FIELD
        ) {
            return true
        }
        return false
    }

    private fun makeTaleMove() {
        var tempTalePart: PartOfTale? = null
        for (index in 0 until allTale.size) {
            val talePart = allTale[index]
            container.removeView(talePart.imageView)
            if (index == 0) {
                tempTalePart = talePart
                allTale[index] = PartOfTale(head.top, head.left, drawPartOfTale(head.top, head.left))
            } else {
                val anotherTempPartOfTale = allTale[index]
                tempTalePart?.let {
                    allTale[index] = PartOfTale(it.top, it.left, drawPartOfTale(it.top, it.left))
                }
                tempTalePart = anotherTempPartOfTale
            }
        }
    }
}

enum class Directions {
    UP,
    RIGHT,
    BOTTOM,
    LEFT
}
