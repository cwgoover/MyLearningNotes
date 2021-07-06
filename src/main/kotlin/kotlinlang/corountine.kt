package kotlinlang

import kotlinx.coroutines.*

/**
 * Created by i352072(erica.cao@sap.com) on 07/06/2021
 */
class corountine {

    suspend fun fetchTwoDocs() {
        coroutineScope {
            launch { fetchDoc(1) }
            async { fetchDoc(2) }
        }

//        GlobalScope.launch {
//            delay(1000L)
//            println("World!")
//        }
//        println("Hello, ") // main thread continues here immediately
//        runBlocking {
//            delay(2000L)  // ... delay for 2 seconds to keep JVM alive
//        }
    }

    private suspend fun fetchDoc(num: Int) {

    }

}