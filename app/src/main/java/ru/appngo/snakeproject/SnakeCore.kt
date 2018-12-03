package ru.appngo.snakeproject

object SnakeCore {
    var nextMove: () -> Unit = {}
    var isPlay = true
    private val thread: Thread

    init {
        thread = Thread(Runnable {
            while (true) {
                Thread.sleep(500)
                if (isPlay) {
                    nextMove()
                }
            }
        })
        thread.start()
    }

    fun startTheGame() {
        isPlay = true
    }
}
