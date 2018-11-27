package ru.appngo.snakeproject

object SnakeCore {
    var nextMove: () -> Unit = {}
    var isPlay = true

    fun startTheGame() {
        Thread(Runnable {
            while (true) {
                Thread.sleep(500)
                if (isPlay) {
                    nextMove()
                }
            }
        }).start()
    }
}
