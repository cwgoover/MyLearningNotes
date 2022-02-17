package kotlinlang.coroutine

import kotlinlang.coroutine.CoroutineFundamentalsYT2.cancelInCooperativeInTheWay1
import kotlinlang.coroutine.CoroutineFundamentalsYT2.cancelInCooperativeInTheWay2
import kotlinlang.coroutine.CoroutineFundamentalsYT2.coroutineHandingExceptions
import kotlinlang.coroutine.CoroutineFundamentalsYT2.coroutineWithTimeout
import kotlinx.coroutines.*

/**
 * Created by i352072(erica.cao@sap.com) on 01/08/2022
 */
object CoroutineFundamentalsYT2 {

    // Kotlin Coroutine Cancellation: Cooperative cancellation, Handle Exceptions, and Timeouts
    // Youtube: https://www.youtube.com/watch?v=CswqyZJikxI
    fun coroutineCancelDescription() = runBlocking {
        // There are basically two ways to make a coroutine cooperative:
        //  1. Periodically invoke a suspending function that checks for cancellation.
        //     - Only those suspending functions that belong to kotlin.coroutines package will make coroutine cooperative
        //     - delay(), yield(), withContext(), withTimeout() etc. are the suspending functions that belong to kotlinx.coroutines package
        //     - If you use these functions in your coroutine, then your coroutine will become cooperative, and therefore it can be cancelled by calling the cancel()
        //  2. Explicitly check for the cancellation status within the coroutine
        //     - CoroutineScope.isActive boolean flag (active: isActive = true; cancelled: isActive = false)
        //     - In case of Threads, we didn't have any such flags, so we have got a provision for terminating a coroutine based on a boolean flag. This is another advantage of using coroutine in place of threads.

        val job = launch {
            // ..the code has to be cooperative in order to get cancelled..
        }
        job.cancel() // If the coroutine is cooperative then cancel it
        // In short, this join() will be effective only when this cancel() fails to cancel the coroutine.
        job.join()   // Waits for the coroutine to finish

        // If the coroutine is cooperative then cancel it else, if it is not cooperative
        // then wait for the coroutine to finish.
        job.cancelAndJoin()
    }

    fun cancelInCooperativeInTheWay1() = runBlocking {   // Creates a blocking coroutine that executes in current thread (main)
        println("Main program starts: ${Thread.currentThread().name}")  // main thread

        val job: Job = launch { // Thread main: Creates a non-blocking coroutine
            println("Fake coroutine work starts: ${Thread.currentThread().name}") // Thread: main
            for (i in 0..500) {
                print("$i.")
//                Thread.sleep(50)  // Not belong to the coroutine package, so can't cooperative to cancel job
//                delay(50)     // it's a suspending function, so this job can cancellable.
                yield()     // or use delay() or any other suspending function as per your need.
            }
        }
    
//        delay(200)  // Let's print a few values before we cancel
        delay(10)   // yield() function doesn't delay job, so need few time to wait.
//        job.cancel()    // Since coroutine is going to get launched in the background thread, hence it could immediately get cancelled.
//        job.join()  // waits for coroutine to finish
        job.cancelAndJoin()

        println("\nMain program ends: ${Thread.currentThread().name}")  // main thread
    }

    fun cancelInCooperativeInTheWay2() = runBlocking {
        println("Main program starts: ${Thread.currentThread().name}")  // main thread

        val job: Job = launch(Dispatchers.Default) { // Thread T1: Creates a non-blocking coroutine
            println("Fake coroutine work starts: ${Thread.currentThread().name}") // Thread: T1
            for (i in 0..500) {
                if (!isActive) {    // check if the flag is false, then let's break out of the loop, and if the loop terminate the eventual coroutine will finish
//                    break
                    return@launch   // when the coroutine is cancelled, then we return from this launch coroutine level.
                }
                print("$i.")
                Thread.sleep(1) // delay this coroutine job without methods in coroutine package
            }
        }

        delay(10)   // yield() function doesn't delay job, so need few time to wait.
        job.cancelAndJoin()

        println("\nMain program ends: ${Thread.currentThread().name}")  // main thread
    }

    fun coroutineHandingExceptions() = runBlocking {
        // 1. Cancellable suspending functions such as yield(), delay() etc throw CancellationException on the coroutine cancellation.
        // 2. You can't execute a suspending function from the finally block because the coroutine running this code is already cancelled.
        // 3. If you really want to execute a suspending function from a finally block, then wrap the code within withContext(NonCancellable) function.
        // 4. You can print your own cancellation message using job.cancel(CancellationException("My crash message."))

        println("Main program starts: ${Thread.currentThread().name}")  // main thread

        val job: Job = launch(Dispatchers.Default) { // Thread T1: Creates a non-blocking coroutine
            println("Fake coroutine work: ${Thread.currentThread().name}") // Thread: T1
            try {
                for (i in 0..500) {
                    print("$i.")
                    delay(5)
                }
            } catch (ex: CancellationException) {
                print("\nException caught safely: ${ex.message}")
            } finally {
                withContext(NonCancellable) {   // this withContext just like launch() creates another coroutine in a new background thread.
                    println("\nFinally coroutine work: ${Thread.currentThread().name}")
                    delay(1000)     // Generally we don't use suspending function in finally
                }
                print("\nClose resources in finally")
            }
        }

        delay(20)
//        job.cancelAndJoin() // when we cancel coroutine, the output from the catch block and from finally block would be printed.
        
        // Creates your own cancellation message
        job.cancel(CancellationException("My own crash message"))
        job.join()

        println("\nMain program ends: ${Thread.currentThread().name}")  // main thread
    }
    
    fun coroutineWithTimeout() = runBlocking {
        // withTimeout(), withTimeoutOrNull() are basically coroutine builders, similar to launch() and async() to create a new coroutine.
        // withTimeout() will throw TimeoutCancellationException if task is timeout.
        // withTimeoutOrNull() is not going to throw any exception, but returns a value at the end.
    
        println("Main program starts: ${Thread.currentThread().name}")  // main thread
        
        println("***** withTimeoutOrNull() *****")
        // this data type should be nullable type, so that it can store a null object, if timeout.
        val result: String? = withTimeoutOrNull(2000) {
            for (i in 0..500) {
                print("$i.")
                delay(500)
            }
            "I am done!"
        }
        println("\nResult: $result")    // output is "null", because it's timeout, so can't return its returned value.
        
        println("\n***** withTimeout() *****")
        withTimeout(2000) { // Then, if the task within coroutine doesn't get over within this time frame, an exception will be thrown.
            try {
                for (i in 0..500) {
                    print("$i.")
                    delay(500)
                }
            } catch (ex: TimeoutCancellationException) {
                println("\nException caught safely: ${ex.message}")
            } finally {
                println("Close resources in finally")
            }
        }
        
        println("\nMain program ends: ${Thread.currentThread().name}")  // main thread
    }
}

fun main() {
    cancelInCooperativeInTheWay1()

    println("================================================")
    cancelInCooperativeInTheWay2()

    println("================================================")
    coroutineHandingExceptions()
    
    println("================================================")
    coroutineWithTimeout()
}