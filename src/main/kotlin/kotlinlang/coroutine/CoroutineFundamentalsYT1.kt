package kotlinlang.coroutine

import kotlinlang.coroutine.CoroutineFundamentalsYT1.launchVsAsync
import kotlinlang.coroutine.CoroutineFundamentalsYT1.threadVsCoroutines
import kotlinlang.coroutine.CoroutineFundamentalsYT1.threadVsCoroutinesWithinRunBlockFunIsSameAsAbove
import kotlinx.coroutines.*

/**
 * Created by i352072(erica.cao@sap.com) on 01/06/2022
 */
object CoroutineFundamentalsYT1 {

    // Kotlin Coroutines: Explore what are coroutines in kotlin. Threads vs. Coroutines
    // Youtube: https://www.youtube.com/watch?v=C38lG2wraoo
    fun threadVsCoroutines() {
        // Executes in main thread
        println("Main program starts: ${Thread.currentThread().name}")

//        Thread {    // creates a background thread (worker thread)
//            println("Fake work starts: ${Thread.currentThread().name}")
//            Thread.sleep(1000)  // Pretend to do some work... may be file upload
//            println("Fake work finished: ${Thread.currentThread().name}")
//        }.start()

        GlobalScope.launch {    // creates a background coroutine that runs on a background thread.
            // Thread: T1
            println("Fake coroutine work starts: ${Thread.currentThread().name}") // Thread: T1

            // NOTE: Basically blocks the entire thread, so if some other coroutines are operating
            // within the same thread as this coroutine is operating, then it will basically
            // block that other coroutine as well.
//            Thread.sleep(1000)

            // Coroutine is suspended but Thread: T1 is free (not blocked), meaning the Thread: T1
            // is firstly not blocked, and it can associate with other coroutines to perform some other tasks
            delay(1000) // when executed, not block the thread on which it is operating!

            // Note: This statement runs after suspend function, so it may change thread to execute
            println("Fake coroutine work finished: ${Thread.currentThread().name}") // Either T1 or some other thread.
        }

        // Blocks the current main thread & wait for coroutine to finish (practically not a right way to wait)
//        Thread.sleep(2000)

        runBlocking {   // Creates a coroutine that blocks the current main thread, meaning it
            // basically block the thread in which it is operating.
            delay(2000) // wait for previous coroutine to finish its task (practically not a right way to wait)
        }

        println("Main program ends: ${Thread.currentThread().name}")
    }

    // Using runBlocking, we launch a new coroutine that blocks the main thread,
    // meaning this coroutine runs on the main thread.
    fun threadVsCoroutinesWithinRunBlockFunIsSameAsAbove() = runBlocking {
        // Executes in main thread
        println("Main program starts: ${Thread.currentThread().name}") // main thread

        // But GlobalScope.launch creates a child coroutine in the background thread,
        // meaning this entire code starts executing parallel in a concurrent manner in the background.
        GlobalScope.launch {    // creates a background coroutine that runs on a background thread.
            println("Fake coroutine work starts: ${Thread.currentThread().name}")   // Thread: T1
            delay(1000)
            println("Fake coroutine work finished: ${Thread.currentThread().name}") // Either T1 or some other thread.
        }

        // it is actually present within the scope of runBlocking function.
        // So this will execute in the main thread and hence it will block the
        // main thread and wait
        delay(2000) // main thread
        println("Main program ends: ${Thread.currentThread().name}") // main thread
    }

    // ******************************************************************************

    // Kotlin Coroutine Builders: launch, async, and runBlocking along with GlobalScope companion object
    // Youtube: https://www.youtube.com/watch?v=a56bmbOvfHY

    /**
     * Coroutine Builders:
     *  * launch - GlobalScope.launch{}
     *  * async - GlobalScope.async{}
     *  * runBlocking
     *
     * Note:
     *  * GlobalScope.launch{} creates coroutines at global (app) level. Global Coroutines are
     *      top-level coroutines and can survive the entire life of the application.
     */
    fun coroutineScopeDescription() = runBlocking {
        // Creates coroutine at global (app) level
        GlobalScope.launch {    // *Discouraged. Use only when needed.*
            // File download
            // Play music
        }
        // Creates coroutine at local scope
        launch {                // *By default, use launch{}*
            // Some data computation
            // Login operation
        }
    }


    fun launchVsAsync() = runBlocking {

        println("Main program starts: ${Thread.currentThread().name}") // main thread

        val job1 = GlobalScope.launch {    // Thread: T1
            println("Fake coroutine work #1 starts: ${Thread.currentThread().name}")   // Thread: T1
            delay(1000) // Coroutine is suspended but Thread: T1 is free (not blocked)
            println("Fake coroutine work #1 finished: ${Thread.currentThread().name}") // Either T1 or some other thread.
        }

        // launch coroutine builder is actually present within the scope of the parent runBlocking coroutine builder (main thread here).
        val job2 = launch {    // Thread: main
            println("Fake coroutine work #2 starts: ${Thread.currentThread().name}")   // Thread: main
            delay(1000) // Coroutine is suspended but Thread: main is free (not blocked)
            println("Fake coroutine work #2 finished: ${Thread.currentThread().name}") // Either main thread or some other thread.
        }

//        delay(2000) // main thread: wait for coroutine to finish (practically not a right way to wait)

        // join() waits for the coroutine to finish its execution,
        //  ** only after which the next statement will be executed. **
        job1.join()
        // If job2 is without join(), next statement will be executed before the certain code in job2
//        job2.join()   // * Try to (un)comment this line to see difference. *

        // Using job object, you can even cancel this coroutine.
//        job1.cancel()

        val jobDeferred: Deferred<Int> = async {    // Thread: main
            println("Fake coroutine work #3 starts: ${Thread.currentThread().name}")   // Thread: main
            delay(1000) // Coroutine is suspended but Thread: main is free (not blocked)
            println("Fake coroutine work #3 finished: ${Thread.currentThread().name}") // Either main thread or some other thread.
            15
        }

//        jobDeferred.join()  // Don't want to use return value, just wait for the coroutine to finish
        // await() will wait for coroutine to finish and after it finish, could get desired value
        val num: Int = jobDeferred.await()

        println("Main program ends: ${Thread.currentThread().name}") // main thread
    }


}

// # Knowledge Points:

/**
 * Point #1:
 *  * GlobalScope.launch() is non-blocking in nature whereas
 *  * runBlocking() blocks the thread in which it operates.
 *
 * Point #2: ('launch' coroutine builder[Fire and Forget])
 *  * Launches a new coroutine without blocking the current thread
 *      * Inherits the thread & coroutine scope of the immediate parent coroutine
 *  * Returns a reference to the Job object
 *  * Using Job object you can cancel the coroutine or wait for coroutine to finish
 *
 * Point #3: ('async' coroutine builder)
 *  * Launches a new coroutine without blocking the current thread
 *      * Inherits the thread & coroutine scope of the immediate parent coroutine
 *  * Returns a reference to the Deferred<T> object
 *  * Using Deferred object you can cancel the coroutine, wait for coroutine to finish, or retrieve the returned result.
 *
 *  Point #4:
 *   * runBlocking{} is generally used to write test cases to test suspending functions.
 *      * A suspending function can be called either from a coroutine or from another suspending function.
 *
 */

fun main() {
    threadVsCoroutines()
    println("================================================")
    threadVsCoroutinesWithinRunBlockFunIsSameAsAbove()

    println("================================================")
    launchVsAsync()
}