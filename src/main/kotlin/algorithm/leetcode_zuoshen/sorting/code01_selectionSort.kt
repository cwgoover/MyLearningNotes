package algorithm.leetcode_zuoshen.sorting

import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.random.Random

/**
 * 选择排序, 时间复杂度O(N^2)
 * 从第一个位置开始，找到数组中最小值的位置，然后交换两个数。
 * 然后第二个位置，第三个位置以此类推，从剩下的数组中找到最小值并且交换
 *
 * 选择排序，即从第一个位置开始，依次选择数列中最小的值放在该位置
 *
 * Created by i352072(erica.cao@sap.com) on 01/15/2022
 */
object code01_selectionSort {
    
    fun selectionSort(arr: ArrayList<Int>?) {
        if (arr == null || arr.size < 2) {
            return
        }
        for (i in arr.indices) {    // 0 ~ N-1
            // find out the minimum value in the array i ~ N-1
            var minIndex = i
            for (j in (i + 1) until arr.size) {
                minIndex = if (arr[j] <= arr[minIndex]) j else minIndex
            }
            // sort here
            swap(arr, i, minIndex)
        }
    }
    
    fun swap(arr: ArrayList<Int>, i: Int, j: Int) {
        val tmp = arr[i]
        arr[i] = arr[j]
        arr[j] = tmp
    }
    
    fun getRandomSet(maxValue: Int, size: Int): HashSet<Int> {
        val s = HashSet<Int>(size)
        while (s.size < size) {
            s += Random.nextInt(1, maxValue)
        }
        return s
    }
}