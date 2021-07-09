package kotlinlang.coroutine

import kotlinx.coroutines.*

/**
 * Highlight: A series of post to introduce Coroutines no Android -- 2: getting started
 * @link{ https://medium.com/androiddevelopers/coroutines-on-android-part-ii-getting-started-3bff117176dd }
 *
 * Created by i352072(erica.cao@sap.com) on 07/07/2021
 */
object CoroutineOnAndroid2 {

    private suspend fun fetchTwoDocs() {
        // The coroutineScope builder will suspend itself until all coroutines started inside
        // of it are complete.
        // Because of this, there’s no way to return from fetchTwoDocs until all coroutines started
        // in the coroutineScope builder are complete.
        coroutineScope {
            launch { fetchDoc(1) }
            async { fetchDoc(2) }
        }

        /**
         * The main difference is that a coroutineScope will cancel whenever any of its children fail.
         * So, if one network request fails, all of the other requests are cancelled immediately.
         * If instead you want to continue the other requests even when one fails, you can use a
         * supervisorScope. A supervisorScope won’t cancel other children when one of them fails.
         */
        supervisorScope {
            launch { fetchDoc(3) }
        }
    }

    private suspend fun fetchDoc(num: Int) {}

    /**
     * Structured concurrency guarantees that when a coroutine errors, its caller or scope is notified.
     * The three things that structured concurrency solves for us:
     *  1. Cancel work when it is no longer needed.
     *  2. Keep track of work while it’s running.
     *  3. Signal errors when a coroutine fails.
     *
     *  Here are the guarantees of structured concurrency:
     *  1. When a scope cancels, all of its coroutines cancel.
     *  2. When a suspend fun returns, all of its work is done.
     *  3. When a coroutine errors, its caller or scope is notified.
     *
     * To use coroutines in a ViewModel:
     *  * viewModelScope    (lifecycle-viewmodel-ktx:2.1.0-alpha04)
     *
     * To make more coroutines, any suspend functions can start more coroutines by using another builder:
     *  * coroutineScope
     *  * supervisorScope
     */

    // However, there are situations where errors can get lost in coroutines.
    // This unrelated coroutine scopes in suspend functions is not following the programming practices of structured concurrency.
    // But the structured concurrency is a combination of types and programming practices
    private val unrelatedScope = MainScope()

    // example of a lost error
    // The error is lost in this code because async assumes that you will eventually call await
    // where it will rethrow the exception. However, if you never do call await, the exception will
    // be stored forever waiting patiently waiting to be raised.
    private suspend fun lostError() {
        // async without structured concurrency
        unrelatedScope.async {
            throw InAsyncNoOneCanHearYou("except")
        }
    }

    // If a coroutine started by coroutineScope throws an exception, coroutineScope can throw it to the caller.
    private suspend fun foundError() {
        coroutineScope {
            async {
                throw StructuredConcurrencyWill("throw")
            }
        }
    }

    fun userNeedsDocs() {
        // GlobalScope is unstructured concurrency
        // ** you should only consider unstructured concurrency in rare cases when you need
        // the coroutine to live longer than the calling scope. **
        GlobalScope.launch {
            fetchTwoDocs()
        }
    }

    fun destroy() {
        //  It’s a good idea to then add structure yourself to ensure you keep track of the unstructured coroutines,
        //  handle errors, and have a good cancellation story.
        GlobalScope.cancel()
    }
}


// ----------------------------------  No relevant to Coroutines ---------------------------------------------------
class InAsyncNoOneCanHearYou() : Throwable() {
    var msg: String = ""
    // https://kotlinlang.org/docs/classes.html#secondary-constructors
    // If the class has a primary constructor, each secondary constructor needs to delegate to
    // the primary constructor, either directly or indirectly through another secondary constructor(s).
    // Delegation to another constructor of the same class is done using the this keyword:
    constructor(str: String) : this() {
        msg = str
    }
}

class StructuredConcurrencyWill(): Throwable() {
    constructor(str: String) : this()
}


fun main() {
    CoroutineOnAndroid2.userNeedsDocs()
    CoroutineOnAndroid2.destroy()
}