package algorithm.basic.sorting

import algorithm.basic.sorting.code2_bubbleSort.swap
import kotlin.collections.ArrayList

/**
 * 插入排序，时间复杂度O(N^2), 额外空间复杂度O(1)
 *
 * 插入排序的时间复杂度比较特殊，因为它的时间复杂度会根据数据状况的不同而不同。假设一个数组是倒序的，那么每一次排序所有
 * 位置上的元素都要进行交换，全数组过一遍的时间复杂度O(N^2); 如果一个数组是正序排好的，那么只需将全数组过一遍即可，时间
 * 复杂度为O(N)。修正后的时间复杂度是按照最差情况估计的算法表现，所以是O(N^2)
 *
 * 虽然时间复杂度都是O(N^2)，但是因为选择排序和冒泡排序都是是严格的O(N^2)；所以插入排序比它俩还是要好一点的
 *
 * 插入理念：类似手里有已经排好序的一组牌，又有一张新牌插入；插入时从一边滑到一个位置就可以插入，并且保证维持原来的排序
 *
 * 从0~0 范围开始排序，在范围内用最后一位的数依次跟它前面的数进行比较；当该数小于它前面的数时就交换两个数，一直到
 * 大于或等于前一个数时停止交换；
 * 然后开始扩大范围，从0~0，到0~1，一直到0~N-1；都按照上述原则，只用最后一位跟前面的比较，交换；当停止交换时，相当于
 * 把最后一位数插入到已经排好序的数列里了；这里最后一位可以看做成那张新牌
 *
 *
 * Created by i352072(erica.cao@sap.com) on 02/17/2022
 */
object code03_insertSort {
    
    fun insertSort(arr: ArrayList<Int>?) {
        if (arr.isNullOrEmpty()) {
            return
        }
        
        // 0~0 一定有序(排除)，0~j 需要开始排序
        for (j in 1 until arr.size) {
            // Java:
            // for (int i = j - 1; i >= 0 && arr[i] > arr[i + 1]; i--) swap(arr, i, i + 1)
            // 循环体内是&&符号，表示只要碰到比前一个数字大的就离开退出循环
            
            // Kotlin #1:
            for (i in j - 1 downTo 0) { // i从j的前一位开始算起，然后跟它的后一位进行比较；第一次它的后一位就是j所在的位置
                // 设计时小心数组越界！注意，这里其实0~i已经是有序的了
                if (arr[i + 1] < arr[i]) {  // 当后一个数小于它前面的数时就交换两个数
                    swap(arr, i, i + 1)
                } else {
                    break   // 如果大于等于前一个数时即可停止
                }
            }
            
            // Kotlin #2:
//            var i = j - 1
//            while (i >= 0 && arr[i] > arr[i + 1]) {
//                swap(arr, i, i + 1)
//                i--
//            }
        }
    }

}