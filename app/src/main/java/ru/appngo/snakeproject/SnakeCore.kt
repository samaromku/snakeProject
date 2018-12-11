package ru.appngo.snakeproject

const val START_GAME_SPEED = 500L
const val MINIMUM_GAME_SPEED = 200L

object SnakeCore {
    var nextMove: () -> Unit = {}
    var isPlay = true
    private val thread: Thread
    var gameSpeed = START_GAME_SPEED

    init {
        thread = Thread(Runnable {
            while (true) {
                Thread.sleep(gameSpeed)
                if (isPlay) {
                    nextMove()
                }
            }
        })
        thread.start()
    }

    fun startTheGame() {
        gameSpeed = START_GAME_SPEED
        isPlay = true
    }
}
