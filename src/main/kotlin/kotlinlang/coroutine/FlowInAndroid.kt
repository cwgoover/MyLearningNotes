package kotlinlang.coroutine

import kotlinlang.coroutine.FlowInAndroid.combineWhenOneEmitsAfterSomeDelay
import kotlinlang.coroutine.FlowInAndroid.zipWhenOneEmitsAfterSomeDelay
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Created by i352072(erica.cao@sap.com) on 12/28/2021
 */
object FlowInAndroid {

    /**
     * What is Flow in Kotlin and how to use it in Android Project?
     * https://blog.mindorks.com/what-is-flow-in-kotlin-and-how-to-use-it-in-android-project
     */
    lateinit var flow: Flow<Int>
    private fun setFlow(name: String) {
        flow = flow {
            println("Start $name flow...")
            // We will emit numbers from 0 to 10 at 500ms delay.
            (0..10).forEach {
                delay(500)
                println("$name Emitting $it")
                // collects the value emitted. It is part of FlowCollector which can be used as a receiver.
                emit(it)
            }
        }.map {
            it * it
        // we use flowOn operator which means that shall be used to change the context of the flow emission.
        // Here, we can use different Dispatchers like IO, Default, etc.
        }.flowOn(Dispatchers.Default)
    }

    fun buildersInFlow() {
        // flow{} - It's normal style

        // flowOf() - It is used to create flow from a given set of values.
        flowOf(4, 2, 5, 1, 7).onEach { delay(400) }.flowOn(Dispatchers.Default)

        // asFlow() - It is an extension function that helps to convert type into flows.
        (1..5).asFlow().onEach { delay(300) }.flowOn(Dispatchers.Default)

        // channelFlow{} - This builder creates cold-flow with the elements using send provided by the builder itself.
        channelFlow {
            for (i in (0..10)) {
                send(i)
            }
        }.flowOn(Dispatchers.Default)
    }

    fun collect() {
        setFlow("collect")
        runBlocking<Unit> {
            launch {
                flow.collect {
                    println("collect: $it")
                }
            }
        }
    }

    // Can't work, nothing run in the launch statement, why?????
    fun collectOrign() {
//        setFlow("collectOrign")
        // Exception: Module with the Main dispatcher had failed to initialize. For tests Dispatchers.setMain from kotlinx-coroutines-test module
//        CoroutineScope(Dispatchers.Main).launch {
        CoroutineScope(Dispatchers.Default).launch {
            // flow.collect now will start extracting/collection the value from the flow
            // on the Main thread as Dispatchers.Main is used in launch coroutine builder
            // in CoroutineScope
            flow.collect {
                println("collectOrign: $it")
            }
        }
    }

    suspend fun zip() {
        val flowOne = flowOf("Himanshu", "Amit", "Janishar").flowOn(Dispatchers.Default)
        val flowTwo = flowOf("Singh", "Shekhar", "Ali").flowOn(Dispatchers.Default)
        // flowOne is zipped with flowTwo to give *a pair of values* that we have created a string
        flowOne.zip(flowTwo) { firstString, secondString ->
            "$firstString $secondString"
        }.collect {
            println(it)
        }
    }

    /**
     * Kotlin Flow operators - Zip vs Combine
     * https://www.youtube.com/watch?v=_1xH9d7w_tA
     */
    fun zipNormalExample() {
        val nums = (1..3).asFlow()
        val strs = flowOf("one", "two", "three")
        runBlocking {
            nums.zip(strs) { a, b -> "$a -> $b" }   // compose a single string with "zip"
                .collect { value ->     // collect and print
                    println(value)
                }
        }
    }

    // Consider if both flows doesn't have the same number of item, then the flow will
    // stop as soon as one of the flow completes.
    fun zipWhenOneCompletesBeforeAnother() {
        val nums = (1..3).asFlow()
        val strs = flowOf("one", "two", "three", "four")
        runBlocking {
            nums.zip(strs) {a, b -> "$a -> $b" }   // compose a single string with "zip"
                .collect { value ->
                    println(value)
                }
        }
    }

    // whenever we work on zip, it will wait for both the flows to emit values and
    // then only it will work
    fun zipWhenOneEmitsAfterSomeDelay() {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
        val strs = flowOf("one", "two", "three").onEach { delay(400) }
        runBlocking {
            nums.zip(strs) {a, b -> "$a -> $b" }.collect { value ->
                println(value)
            }
        }
    }

    // You have to make it very clear that it combines the most recently emitted values by each flow.
    // and it has to recompute the result whenever any of the upstream flows emits a new value
    fun combineWhenOneEmitsAfterSomeDelay() {
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
        val strs = flowOf("one", "two", "three").onEach { delay(400) }
        runBlocking {
            // compose a single string with "combine"
            nums.combine(strs) { a, b -> "$a -> $b" }.collect { value ->
                println(value)
            }
        }
    }

    /**
     * Downloading an Image
     * https://blog.mindorks.com/creating-flow-using-flow-builder-in-kotlin
     *
     * It will download the Image in the background thread and keep sending the progress
     * to the collector on the Main thread.
     */
    fun downlaodImageFlow() {
        val downloadImageFlow = flow {
            // our image downloading code
            // start downloading
            // send progress
            emit(10)
            // downloading...
            // ......
            // ......
            // send progress
            emit(75)
            // downloading...
            // ......
            // ......
            // send progress
            emit(100)
        }.flowOn(Dispatchers.Default)

        CoroutineScope(Dispatchers.Main).launch {
            downloadImageFlow.collect {
                // we will get the progress here
            }
        }
    }

}

/**
 * Understanding Terminal Operators in Kotlin Flow
 * https://blog.mindorks.com/terminal-operators-in-kotlin-flow
 * Terminal operators are the operators that actually start the flow by connecting the flow builder,
 * operators with the collector.
 *      * collect()
 *      * reduce() - apply a function to each item emitted and emit the final value
 */
fun main() {
//    println("Run collectOrign:")
//    FlowInAndroid.collectOrign()
//
//    println("\nRun collect:")
//    FlowInAndroid.collect()

    println("================================================")
    FlowInAndroid.zipNormalExample()

    println("================================================")
    FlowInAndroid.zipWhenOneCompletesBeforeAnother()

    println("================================================")
    zipWhenOneEmitsAfterSomeDelay()

    println("================================================")
    combineWhenOneEmitsAfterSomeDelay()
}