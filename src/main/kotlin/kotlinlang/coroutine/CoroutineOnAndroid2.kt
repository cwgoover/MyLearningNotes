package kotlinlang.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * Highlight: A series of post to introduce Coroutines no Android -- 2: getting started
 * @link{ https://medium.com/androiddevelopers/coroutines-on-android-part-ii-getting-started-3bff117176dd }
 *
 * Created by i352072(erica.cao@sap.com) on 07/07/2021
 */
object CoroutineOnAndroid2 {

    /**
     * However, coroutines by themselves don’t help you keep track of the work that’s being done.
     *
     * It’s quite difficult to keep track of one thousand coroutines manually using code.
     * You could try to track all of them and manually ensure they complete or cancel,
     * but code like this is tedious and error prone. If the code is not perfect, it will lose
     * track of a coroutine, which is what I call a work leak.
     *
     *  A work leak is like a memory leak, but worse.
     *  A leaked coroutine can waste memory, CPU, disk, or even launch a network request that’s not needed.
     *
     *  To help avoid leaking coroutines, Kotlin introduced [structured concurrency].
     *  [Structured concurrency] is a combination of language features and best practices that,
     *  when followed, help you keep track of all work running in coroutines.
     *
     *  On Android, we can use [structured concurrency] to do three things:
     *      * *Cancel work* when it is no longer needed.
     *      * *Keep track* of work while it’s running.
     *      * *Signal errors* when a coroutine fails.
     */


    // ----- Cancel work with scopes -----

    /**
     * In Kotlin, coroutines must run in something called a [CoroutineScope].
     * A [CoroutineScope] keeps track of your coroutines, even coroutines that are suspended.
     * Unlike the [Dispatchers] we talked about in part one, it doesn’t actually execute your
     * coroutines — it just makes sure you don’t lose track of them.
     *
     * To ensure that all coroutines are tracked, Kotlin does not allow you to start a new coroutine without a [CoroutineScope].
     * It grants you the ability to start new coroutines which come with all that suspend and resume
     * goodness we explored in part one.
     *
     * A [CoroutineScope] keeps track of all your coroutines, and it can cancel all of the coroutines started in it.
     */

    // #1. Starting new coroutines

    /**
     * It’s important to note that you can’t just call a suspend function from anywhere.
     * The [suspend] and [resume] mechanism requires that you switch from normal functions to a coroutine.
     *
     * There are two ways to start coroutines, and they have different uses:
     *  * [launch] builder will start a new coroutine that is “fire and forget”
     *          — that means it won’t return the result to the caller.
     *  * [async] builder will start a new coroutine, and it allows you to return a result with a suspend function called [await].
     *
     *  In almost all cases, the right answer for how to start a coroutine from a regular function
     *  is to use [launch]. Since the regular function has no way to call [await] (remember,
     *  it can’t call suspend functions directly) it doesn’t make much sense to use [async] as your
     *  main entry to coroutines.
     */

    // Launch is a bridge from regular functions into coroutines.
    private suspend fun startNewCoroutines() {
        /** You should instead use a coroutine scope to start a coroutine by calling [launch]. */
        GlobalScope.launch {
            // This block starts a new coroutine
            // "in" the scope.
            //
            // It can call suspend functions
            fetchDoc(0)
        }
    }

    /**
     * Warning: A big difference between [launch] and [async] is how they handle exceptions.
     * [async] expects that you will eventually call [await] to get a result (or exception) so it
     * won’t throw exceptions by default. That means if you use [async] to start a new coroutine
     * it will silently drop exceptions.
     *
     * Since [launch] and [async] are only available on a [CoroutineScope], you know that any
     * coroutine you create will always be tracked by a scope. Kotlin just doesn’t let you create
     * an untracked coroutine, which goes a long way to avoid work leaks.
     */

    // #2. Start in the ViewModel

    /**
     * On Android, it often makes sense to associate a [CoroutineScope] with a user screen.
     * This lets you avoid leaking coroutines or doing extra work for Activities or Fragments
     * that are no longer relevant to the user.
     *
     * Structured concurrency guarantees when a scope cancels, all of its coroutines cancel.
     *
     * When integrating coroutines with Android Architecture Components, you typically want to
     * launch coroutines in the [ViewModel] — you won’t have to worry about rotation killing all
     * your coroutines.
     */

    class MyViewModel(): ViewModel() {
        fun userNeedsDocs() {
            // Start a new coroutine in a ViewModel
            viewModelScope.launch {
                /** [viewModelScope] will automatically cancel any coroutine that is started by this
                 *  ViewModel when it is cleared (when the [onCleared()] callback is called).
                 *
                 *  And because for more safety, a [CoroutineScope] will propagate itself.
                 *  So, if a coroutine you start goes on to start another coroutine, they’ll both
                 *  end up in the same scope. That means even when libraries that you depend on start
                 *  a coroutine from your [viewModelScope], you’ll have a way to cancel them! */
                fetchDoc(0)
            }
        }

        /**
         * Warning: Coroutines are cancelled cooperatively by throwing a [CancellationException] when
         * the coroutine is suspended. Exception handlers that catch a top-level exception like [Throwable]
         * will catch this exception. If you consume the exception in an exception handler, or never
         * suspend, the coroutine will linger in a semi-canceled state.
         */

        /**
         * So, when you need a coroutine to run as long as a [ViewModel], use [viewModelScope] to
         * switch from regular functions to coroutines. Then, since [viewModelScope] will automatically
         * cancel coroutines for you, it’s totally fine to write an infinite loop here without creating leaks.
         */
        private fun runForever() {
            // start a new coroutine in the ViewModel
            viewModelScope.launch {
                // cancelled when the ViewModel is cleared
                while (true) {
                    delay(1000)
                    // do something every second
                }
            }
        }
    }


    // ----- Keep track of work -----

    /**
     * Sometimes, though, you need a bit more complexity. Say you wanted to do two network requests
     * simultaneously (or at the same time) in a coroutine — to do that you’ll need to start more coroutines!
     *
     * To make more coroutines, any suspend functions can start more coroutines by using another
     * builder called [coroutineScope] or its cousin [supervisorScope].
     */

    /**
     * Launching new coroutines everywhere is one way to create potential work leaks.
     * The caller may not know about the new coroutines, and if it doesn’t how could it keep track of the work?
     * To fix this, structured concurrency helps us out. Namely, it provides a guarantee that when
     * a suspend function returns, all of its work is done.
     */

    /** [coroutineScope] and [supervisorScope] let you safely launch coroutines from suspend functions. */


    // In this example, two documents are fetched from the network simultaneously.
    // To make structured concurrency and avoid work leaks, we want to ensure that when a suspend
    // function like fetchTwoDocs returns, all of its work is done. That means both of the coroutines
    // it launches must complete before fetchTwoDocs returns.
    private suspend fun fetchTwoDocs() {
        /**
         * Kotlin ensures that the work does not leak from [fetchTwoDocs] with the [coroutineScope] builder.
         *
         * The [coroutineScope] builder will suspend itself until all coroutines started inside
         * of it are complete.
         * Because of this, there’s no way to return from [fetchTwoDocs] until all coroutines started
         * in the [coroutineScope] builder are complete.
         */
        coroutineScope {
            launch { fetchDoc(1) }  // “fire and forget” — means it won’t return the result to the caller.
            async { fetchDoc(2) }   // the document can be returned to the caller
        }

        /** [coroutineScope] and [supervisorScope] will wait for child coroutines to complete. */

        /**
         * The main difference is that a [coroutineScope] will [cancel] whenever any of its children fail.
         * So, if one network request fails, all of the other requests are cancelled immediately.
         * If instead you want to continue the other requests even when one fails, you can use a
         * [supervisorScope]. A [supervisorScope] won’t cancel other children when one of them fails.
         */
        supervisorScope {
            launch { fetchDoc(3) }
        }
    }

    /**
     * What’s important is that using [coroutineScope] or [supervisorScope] you can [launch] a
     * coroutine safely from any suspend function.
     *
     * What’s really cool is [coroutineScope] will create a child scope.
     * So if the parent scope gets cancelled, it will pass the cancellation down to all the new
     * coroutines. If the caller was the [viewModelScope], all coroutines would be automatically
     * cancelled when the user navigated away from the screen. Pretty neat!
     */

    private suspend fun fetchDoc(num: Int) {}


    // ----- Signal errors when a coroutine fails -----

    /**
     * In coroutines, errors are signaled by throwing exceptions, just like regular functions.
     * Exceptions from a suspend function will be re-thrown to the caller by resume.
     * Just like with regular functions, you’re not limited to try/catch to handle errors and
     * you can build abstractions to perform error handling with other styles if you prefer.
     */

    // However, there are situations where errors can get lost in coroutines.

    /**
     * example of a lost error
     * The error is lost in this code because [async] assumes that you will eventually call [await]
     * where it will rethrow the exception. However, if you never do call [await], the exception will
     * be stored forever waiting patiently waiting to be raised.
     */
    private suspend fun lostError() {
        // async without structured concurrency
        unrelatedScope.async {
            // Note this code is declaring an unrelated coroutine scope that will launch a new
            // coroutine without structured concurrency.
            throw InAsyncNoOneCanHearYou("except")
        }
    }

    // This unrelated coroutine scopes in suspend functions is not following the programming practices of structured concurrency.
    // Since the structured concurrency is a combination of types and programming practices
    private val unrelatedScope = MainScope()

    /**
     * Structured concurrency guarantees that when a coroutine errors, its caller or scope is notified.
     */

    /**
     * Since the [coroutineScope] will wait for all children to complete, it can also get notified
     * when they fail. If a coroutine started by [coroutineScope] throws an exception, [coroutineScope]
     * can throw it to the caller.
     */
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


    // ----- Using structured concurrency -----

    /**
     * You can create unstructured concurrency by introducing a new unrelated [CoroutineScope]
     * (note the capital C), or by using a global scope called [GlobalScope], but you should only
     * consider unstructured concurrency in rare cases when you need the coroutine to live longer
     * than the calling scope. It’s a good idea to then add structure yourself to ensure you keep
     * track of the unstructured coroutines, handle errors, and have a good cancellation story.
     */

    /**
     * Structured concurrency guarantees that when a coroutine errors, its caller or scope is notified.
     *
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
     *  * [viewModelScope]    (lifecycle-viewmodel-ktx:2.1.0-alpha04)
     *
     * To make more coroutines, any suspend functions can start more coroutines by using another builder:
     *  * [coroutineScope]
     *  * [supervisorScope]
     */
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