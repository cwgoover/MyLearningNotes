package kotlinlang.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by i352072(erica.cao@sap.com) on 12/23/2021
 */
class CoroutineSamples {

    fun channelTest() = runBlocking {
        // Channel就是协程数据通信的一个重要手段
        val channel = Channel<Int>()
        launch {
            println("start send value")
            for (x in 1..5) {
                println("send value ${x*x}")
                channel.send(x * x)
            }
            println("end send value")
        }

        repeat(5) {
            println(channel.receive().toString())
        }
        println(" Done ! ")
    }
}

fun main() {
    CoroutineSamples().channelTest()
}