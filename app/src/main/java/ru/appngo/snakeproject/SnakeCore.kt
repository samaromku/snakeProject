package ru.appngo.snakeproject

object SnakeCore {
    var nextMove: () -> Unit = {}

    fun startTheGame() {
        Thread(Runnable {
            while (true) {
                Thread.sleep(500)
                nextMove()
            }
        }).start()
    }
}
