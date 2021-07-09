package kotlinlang.coroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.launch

/**
 * Highlight: A series of post to introduce Coroutines no Android -- 3: real work
 * @link{ https://medium.com/androiddevelopers/coroutines-on-android-part-iii-real-work-2ba8a2ec2f45 }
 *
 * Created by i352072(erica.cao@sap.com) on 07/07/2021
 */
object CoroutineOnAndroid3 {

    /**
     * There are two types of tasks that come up all the time in real code that coroutines are a great solution for:
     *  1. One shot requests: are requests that are run each time they are called — they always
     *      complete after the result is ready.
     *  2. Streaming requests: are requests that continue to observe changes and report them to
     *      caller — they don’t complete when the first result is ready.
     */

    // ----- One shot requests -----

    /** As a general pattern, start coroutines in the ViewModel. */

    // The ViewModel is typically the right place to start most coroutines in this architecture,
    // since it can cancel the coroutine in [onCleared].
    class ProductsViewModel(private val productsRepository: ProductsRepository): ViewModel() {
        private val _sortedProducts = MutableLiveData<List<ProductListing>>()
        val sortedProducts: LiveData<List<ProductListing>> = _sortedProducts

        /**
         * Bug report: When starting a new coroutine in response to a UI event, consider what happens
         *      if the user starts another before this one completes.
         * ===> The best solution: Disable the button
         *      The easiest way to do that is to disable the sort buttons to stop the new events.
         */

        // Solution 0: Disable the sort buttons when any sort is running
        private val _sortButtonsEnabled = MutableLiveData<Boolean>()
        val sortButtonsEnabled: LiveData<Boolean> = _sortButtonsEnabled

        init {
            _sortButtonsEnabled.value = true
        }

        /**
         * Called by the UI when the user clicks the appropriate sort button
         */
        fun onSortAscending() = sortPricesBy(ascending = true)
        fun onSortDescending() = sortPricesBy(ascending = false)

        // Note: Some background save operations may want to continue after user leaves a screen —
        // and it makes sense to have those saves run without a lifecycle.
        // In most other cases the viewModelScope is a reasonable choice.
        private fun sortPricesBy(ascending: Boolean) {
            viewModelScope.launch {
                // disable the sort buttons whenever a sort is running
                _sortButtonsEnabled.value = false
                try {
                    // suspend and resume make this database request main-safe
                    // so our ViewModel doesn't need to worry about threading
                    _sortedProducts.value =
                        productsRepository.loadSortedProducts(ascending)
                } finally {
                    // re-enable the sort buttons after the sort is complete
                    _sortButtonsEnabled.value = true
                }
            }
        }
    }

    /** A repository should prefer to expose regular suspend functions that are main-safe. */

    // Since a repository doesn’t have a natural lifecycle — it’s just an object — it would have no way to cleanup work.
    // As a result, any coroutines started in the repository will leak by default.
    open class ProductsRepository(val productsDao: ProductsDao) {
        /**
         * This is a "regular" suspending function, which means the caller must
         * be in a coroutine. The repository is not responsible for starting or
         * stopping coroutines since it doesn't have a natural lifecycle to cancel
         * unnecessary work.
         *
         * This *may* be called from Dispatchers.Main and is main-safe because
         * Room will take care of main-safety for us.
         */
        open suspend fun loadSortedProducts(ascending: Boolean): List<ProductListing> {
            return if (ascending) {
                productsDao.loadProductsByDateStockedAscending()
            } else {
                productsDao.loadProductsByDateStockedDescending()
            }
        }
        // Whenever the Repository has to do expensive things like transform a list it should
        // use withContext to expose a main-safe interface.
    }

    /**
     * But:
     *  If you switched dispatchers, a fast-fingered user on a slow phone could send more than one click!
     * ==> the rest of this post exploring a few different options.
     */

    // ----- Concurrency patterns -----
    /**
     * There are three basic patterns that you can use for a one shot request to ensure that
     * exactly one request runs at a time:
     *  1. *Cancel previous work* before starting more.
     *  2. *Queue the next work* and wait for the previous requests to complete before starting another one.
     *  3. *Join previous work* if there’s already a request running just return that one instead of starting another request.
     *
     *  https://gist.github.com/objcode/7ab4e7b1df8acd88696cb0ccecad16f7#file-concurrencyhelpers-kt-L19
     */
    class AdvancedProductsRepository(val dao: ProductsDao, val productsApi: ProductsService) : ProductsRepository(dao) {

        // **** Solution #1: Cancel previous work //

        // This is a great solution for tasks like sorting and filtering that can be cancelled if a new request comes in.
        // Important: This pattern is not well suited for use in global singletons,
        // since unrelated callers shouldn’t cancel each other.
        private var controlledRunner = ControlledRunner<List<ProductListing>>()

        /**
         * Consider building abstractions to avoid mixing ad-hoc concurrency patterns with application code.
         * Looking into [kotlinlang.coroutine.ControlledRunner.cancelPreviousThenRun] is a good way
         * to see how to keep track of in-progress work.*
         */
        override suspend fun loadSortedProducts(ascending: Boolean): List<ProductListing> {
            // cancel the previous sorts before starting a new one
            return controlledRunner.cancelPreviousThenRun {
                if (ascending) {
                    productsDao.loadProductsByDateStockedAscending()
                } else {
                    productsDao.loadProductsByDateStockedDescending()
                }
            }
        }


        // **** Solution #2: Add a Mutex //

        // There’s one solution to concurrency bugs that always works.

        // Just queue up requests so only one thing can happen at a time! Just like a queue or
        // a line at a store, requests will execute one at a time in the order they started.

        // Note: This is not optimal for the specific use case of sorting
        // or filtering but is a good pattern for network saves.
        private val singleRunner = SingleRunner()

        // Whenever a new sort comes in, it uses a instance of SingleRunner to ensure that
        // only one sort is running at a time.
        suspend fun loadSortedProductsMutex(ascending: Boolean): List<ProductListing> {
            // wait for the previous sort to complete before starting a new one
            return singleRunner.afterPrevious {
                // A Mutex lets you ensure only one coroutine runs at a time — and they will finish
                // in the order they started.
                if (ascending) {
                    productsDao.loadProductsByDateStockedAscending()
                } else {
                    productsDao.loadProductsByDateStockedDescending()
                }
            }
        }


        // **** Solution #3: Join previous work //

        // It’s a good idea if the new request would re-start the exact same work that has already been half completed.

        // This pattern doesn’t make very much sense with the sort function, but it’s a natural fit
        // for a network fetch that loads data.

        /**
         * If there’s already a request running, it waits for the result of current “in flight”
         * request and returns that instead of running a new one.
         * The block will only be executed if there was not already a request running.
         *
         * Join previous work is a great solution to avoiding repeated network requests.
         */
        suspend fun fetchProductsFromBackend(ascending: Boolean): List<ProductListing> {
            // if there's already a request running, return the result from the
            // existing request. If not, start a new request by running the block.
            return controlledRunner.joinPreviousOrRun {
                val result = productsApi.getProductions()
                productsDao.insertAll(result)
                // it just returns the previous result if there’s anything in activeTask
                result
            }
        }
    }



    // ----------------------------------  No relevant to Coroutines ---------------------------------------------------
    // Room & Coroutines
    // https://medium.com/androiddevelopers/room-coroutines-422b786dc4c5
    @Dao
    interface ProductsDao {
        // Because this is marked suspend, Room will use it's own dispatcher
        //  to run this query in a main-safe way.
        // That means you can call them directly from Dispatchers.Main.
        @Query("select * from ProductListing ORDER BY dateStocked ASC")
        suspend fun loadProductsByDateStockedAscending(): List<ProductListing>

        // Because this is marked suspend, Room will use it's own dispatcher
        //  to run this query in a main-safe way.
        // That means you can call them directly from Dispatchers.Main.
        @Query("select * from ProductListing ORDER BY dateStocked DESC")
        suspend fun loadProductsByDateStockedDescending(): List<ProductListing>

        /**
         * Note: Room uses its own dispatcher to run queries on a background thread. Your code
         * should not use withContext(Dispatchers.IO) to call suspending room queries.
         * ** It will complicate the code and make your queries run slower.**
         */

        fun insertAll(data: List<ProductListing>)
    }

    data class ProductListing(val str: String)

    class ProductsService {
        fun getProductions(): List<ProductListing> {
            return emptyList()
        }
    }

}