package kotlinlang.coroutine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Highlight: A series of post to introduce Coroutines no Android -- 1: Getting the background
 * @link{ https://medium.com/androiddevelopers/coroutines-on-android-part-i-getting-the-background-3e0e54d20bb }
 *
 * On Android, you can use them to solve two really common problems:
 *  * Simplifying the code for long running tasks such as reading from the network,
 *      disk, or even parsing a large JSON result.
 *
 *  * Performing precise main-safety to ensure that you never accidentally block the main thread
 *      without making code difficult to read and write.
 *
 * Created by i352072(erica.cao@sap.com) on 07/07/2021
 */
object CoroutineOnAndroid {

    //  The concept of coroutines has been around since the dawn of programming languages. The first
    //  language to explore using coroutines was Simula in 1967.
    // Javascript, C#, Python, Ruby, and Go to name a few. Kotlin coroutines are based on established
    // concepts that have been used to build large applications.

    // On Android, coroutines are a great solution to two problems:
    //  1. *Long running tasks* are tasks that take too long to block the main thread.
    //  2. *Main-safety* allows you to ensure that any suspend function can be called from the main thread.

    // ----- Long running tasks -----

    class TasksViewModel: ViewModel() {
        // #1. Using Callback for long running tasks
        fun fetchDocs1() {
            get("developer.android.com", object : Observer<String> {
                override fun onChanged(t: String) {
                    show(t)
                }
            })
        }

        // #2. Using coroutines for long running tasks
        // Dispatchers.Main
        suspend fun fetchDocs2() {
            // Dispatchers.Main
            val result = get("developer.android.com")
            // Dispatchers.Main
            show(result)
        }

        /**
         * Coroutines build upon regular functions by adding two new operations. In addition to *invoke*
         * (or call) and *return*, coroutines add *suspend* and *resume*.
         *
         *  * suspend — pause the execution of the current coroutine, saving all local variables
         *
         *  * resume — continue a suspended coroutine from the place it was paused
         *
         * Suspend and resume work together to replace callbacks.
         *
         * This functionality is added by Kotlin by the *suspend* keyword on the function.
         * You can only call suspend functions from other suspend functions, or by using a coroutine
         * builder like [launch] to start a new coroutine.
         */

        /**
         * [get] will *suspend* the coroutine before it starts the network request.
         * The function [get] will still be responsible for running the network request off the main thread.
         * Then, when the network request completes, instead of calling a callback to notify the
         * main thread, it can simply resume the coroutine it suspended.
         *
         * Whenever a coroutine is suspended, the current stack frame (the place that Kotlin uses
         * to keep track of which function is running and its variables) is copied and saved for later.
         * When it resumes, the stack frame is copied back from where it was saved and starts running again.
         *
         * when all of the coroutines on the main thread are suspended, the main thread is free to update
         * the screen and handle user events. Together, suspend and resume replace callbacks. Pretty neat!
         *
         * when all of the coroutines on the main thread are suspended, the main thread is free to
         * update the screen and handle user events. Together, suspend and resume replace callbacks.
         * Pretty neat!
         */
        suspend fun get(url: String) : String = withContext(Dispatchers.IO){
            url + "result"
        }
    }

    // ----- Main-safety with coroutines -----

    /**
     * Using [suspend] doesn’t tell Kotlin to run a function on a background thread.
     *
     * **Coroutines will run on the main thread, and suspend does not mean background.**
     *
     * In Kotlin, all coroutines must run in a [dispatcher] — even when they’re running on the main
     * thread. Coroutines can [suspend] themselves, and the [dispatcher] is the thing that knows
     * how to [resume] them.
     *
     * To specify where the coroutines should run, Kotlin provides three Dispatchers you can use
     * for thread dispatch:
     *
    +-----------------------------------+
    |         Dispatchers.Main          |
    +-----------------------------------+
    | Main thread on Android, interact  |
    | with the UI and perform light     |
    | work                              |
    +-----------------------------------+
    | - Calling suspend functions       |
    | - Call UI functions               |
    | - Updating LiveData               |
    +-----------------------------------+

    +-----------------------------------+
    |          Dispatchers.IO           |
    +-----------------------------------+
    | Optimized for disk and network IO |
    | off the main thread               |
    +-----------------------------------+
    | - Database*                       |
    | - Reading/writing files           |
    | - Networking**                    |
    +-----------------------------------+

    +-----------------------------------+
    |        Dispatchers.Default        |
    +-----------------------------------+
    | Optimized for CPU intensive work  |
    | off the main thread               |
    +-----------------------------------+
    | - Sorting a list                  |
    | - Parsing JSON                    |
    | - DiffUtils                       |
    +-----------------------------------+
     */

    /**
     * In this example, [fetchDocs] is executing on the main thread, but can safely call get which
     * performs a network request in the background. Because coroutines support [suspend] and [resume],
     * the coroutine on the main thread will be resumed with the result as soon as the [withContext]
     * block is complete.
     */
    class TasksViewModel2: ViewModel() {
        /** Well written suspend functions are always safe to call from the main thread (or main-safe). */

        // Dispatchers.Main
        suspend fun fetchDocs() {
            // Dispatchers.Main
            val result = get("developer.android.com")
            // Dispatchers.Main
            show(result)
        }

        // Dispatchers.Main
        suspend fun get(url: String): String =
            // Dispatchers.Main
            /** Since [withContext] is itself a suspend function, it will work using coroutines to provide main safety. */
            withContext(Dispatchers.IO) {
                // Dispatchers.IO
                /* perform blocking network IO here */
                url + "result"
            }
            // Dispatchers.Main

        /**
         * So a good practice is to use [withContext] to make sure every function is safe to be
         * called on any [Dispatcher] including [Main] — that way the caller never has to think
         * about what thread will be needed to execute the function.
         *
         * It’s a really good idea to make every suspend function main-safe.
         * If it does anything that touches the disk, network, or even just uses too much CPU,
         * use [withContext] to make it safe to call from the main thread.
         */
    }


    // ----------------------------------  No relevant to Coroutines ---------------------------------------------------
    fun show(msg: String) { println(msg) }
    fun get(url: String, observer: Observer<String>) {
        // many other things to do here
        observer.onChanged(url)
    }
    interface Observer<T> {
        fun onChanged(t: T)
    }

}
