package algorithm.basic.sort

import algorithm.basic.sort.sample02_heapExtendQuestion.sortedArrDistanceLessK
import java.util.PriorityQueue
import kotlin.math.min

/**
 * 题目：
 *  已知一个几乎有序的数组，几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离可以不超过k，并且k相对于数组来说比较小。
 *  请选择一个合适的排序算法针对这个数据进行排序。
 *
 *  A: 假设K=6，即每一个元素从原始位置到它排好之后的位置，移动的距离不会超过6。注意对这句话的理解！移动不超过6的距离一定能排好它的位置！
 *    1. 准备一个小根堆
 *    2. 遍历数组的前七个数，将这0~6七个数字做成一个小根堆；此时小根堆的最小值一定是数组0位置上的数，即小根堆的根节点一定在0位置。
 *          因为任何一个数在排完序之后，它移动的距离不会超过6.说明本该在0位置的数，它最远可能在的位置就是它移动6步后数组中6这个位置；
 *          一定在准备的这个小根堆中！
 *    3. 把小根堆的头结点(最小值)弹出(相当于移除掉)放在数组的0位置上
 *    4. 将数组中7位置的数填充到小根堆中，并重新整理小根堆；那么整理后的小根堆的最小值就是数组1位置上的数
 *    5. 将小根堆的头结点(最小值)弹出(相当于移除掉)放在数组的1位置上
 *    6. 然后依次按顺序从数组中拿元素填充到小根堆里，整理，弹出，放置；重复上面的4，5步
 *    7. 最后数组临近结束时，依次将小根堆中的最小值弹出放置在数组中，直到完成排序
 *
 *  该方法的复杂度 -- O(N*logk), 如果k很小的话，相当于O(N) :
 *    1. 每个数字能排好序的代价是log7, 因为小根堆只放7个数；(数组变成小根堆的复杂度：O(N*logN))
 *    2. 一共N个数，所以复杂度为O(N*log7); 即 O(N*logk)
 *
 * Created by i352072(erica.cao@sap.com) on 05/10/2022
 */
object sample02_heapExtendQuestion {
    
    fun sortedArrDistanceLessK(arr: ArrayList<Int>?, k: Int) {
        if (arr.isNullOrEmpty() || arr.size < 2) {
            return
        }
        
        // 1. 做出一个长度为k，或者arr.size的小根堆
        // https://www.bezkoder.com/kotlin-priority-queue/
        // The front of the Priority Queue contains the least element according to the ordering,
        // and the rear contains the greatest element.
        val minHeap = PriorityQueue<Int>()  // 注意：优先级队列就是堆结构(参看源码)，这里默认就是小根堆！
        // #1 注意第一点，PriorityQueue的每次操作的代价都是O(logN);
        // 另外因为PriorityQueue是由数组保存的，所以如果需要扩容，它每次扩容都会成倍的扩容，而扩容时将原数组拷贝到
        // 新数组的代价是O(N); 但是即使这样，操作的时候算上扩容的代价，整体操作的代价依然是O(logN)。
        // 因为它成倍扩容，很久不需要扩一次;扩容时也就是在某个时刻稍慢一点，但是均摊下来比较低。证明如下：
        // 假设一共加了N个数，就会经历logN次扩容 (假设N=16，2个数超了，翻倍到4，超了翻倍到8，8超了到16，所以扩容的
        // 次数是logN水平)，但是每一次扩容是O(N)水平, 整体扩容代价是O(N*logN); 如果把扩容代价算到每一次添加一个数
        // 时，即均摊下来，复杂度需要除以N: O(N*logN)/N = O(logN)
        // #2 注意第二点，系统提供的堆结构只能提供增加和弹出这样的堆基本操作，不能提供类似改变堆里某个值后，自动再次调整
        // 为堆结构的操作；除非自己手写的堆，可实现类似功能。这就是为什么在很多面试场合不得不自己手写堆的原因；有些情景
        // 下是只有自己手写堆才能做到高效的；但是有些题目只是简单的给它一个数，它弹出一个数，这时候就不需要手写堆。
        
        var index = 0
        // Erica Solution: so stupid!
        // FIXME: Error: need to check the range of size whether is larger than k
//        for (i in 0..k) {
//            minHeap.add(arr[i])
//            index++
//        }
        // 2. 把前k + 1或者arr.size个数放在小根堆里
        // index <= k 时，把先k + 1的数放在堆里去；因为假设移动距离k=6时，0的位置最远可以到0+6=6的位置，即
        // arr[0] ~ arr[6] 共7个数需要在堆里
        while (index <= min(arr.size - 1, k)) {     // 这里min操作用来处理边界条件！
            minHeap.add(arr[index++])
        }
        
        // 3. 把小根堆头结点弹出放在数组0位置上
        var headPoint = 0
        // 4. 将后面的数组元素依次放在小根堆中，整理小根堆，并弹出小根堆的头结点，依次从0位置开始放置
        while (index < arr.size) {
            minHeap.add(arr[index++])
            arr[headPoint++] = minHeap.remove()
        }
        // 其实这里就是加一个，弹一个；利用小根堆的特性
        
        // 5. 当没有元素可以插入时，整理小根堆的头结点，依次弹出放在数组中
        while (minHeap.peek() != null) {
            arr[headPoint++] = minHeap.remove()
        }
    }
}

fun main() {
    val arr = arrayListOf(6, 5, 4, 3, 2, 1, 7, 10, 9, 8)
    println("before sort: $arr")
    sortedArrDistanceLessK(arr, 6)
    println("after sort: $arr")
}