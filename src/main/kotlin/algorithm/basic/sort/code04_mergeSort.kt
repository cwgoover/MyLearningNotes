package algorithm.basic.sort

/**
 * 归并排序，时间复杂度O(N*logN), 额外空间复杂度O(N)
 *
 * 整体就是一个简单递归，数组从中点分两部分，左边排好序、右边排好序，然后merge两边的序列，让整体有序。
 * merge的方法是：两个数组都各从第一位开始进行比较，如果左侧数组的数值小于等于右侧的，copy左侧的值到新创建的数值；
 *   如果左侧值大于右侧的，copy右侧的值到新数组 (谁小copy谁)；copy结束后移动下标到下一位进行比较；直到某一边全部
 *   copy完后，将剩下数组中的元素全部copy到新数组，结束merge
 *
 * 让其整体有序的过程里用了排外序方法
 * 利用master公式来求解时间复杂度
 *
 * Created by i352072(erica.cao@sap.com) on 03/29/2022
 */
object code04_mergeSort {
    
    fun mergeSort(arr: ArrayList<Int>?) {
        if (arr == null || arr.size < 2) {
            return
        }
        process(arr, 0, arr.size - 1)
    }
    
    private fun process(arr: ArrayList<Int>, start: Int, end: Int) {
        if (start == end) return    // 结束条件
    
//        val mid = start + (end - start) shr 1 // Error: 最右边必须带括号，位运算符的优先级低于加减运算符
        val mid = start + ((end - start) shr 1)
        process(arr, start, mid)
        process(arr, mid + 1, end)
        merge(arr, start, mid, end)
    }
    
    private fun merge(arr: ArrayList<Int>, start: Int, mid: Int, end: Int) {
//        val newArray = ArrayList<Int>(arr.size)   // Error: arr的长度一直不变不能直接用，我们需要的是merge部分的数组长度
        val newArray = ArrayList<Int>(end - start + 1)
        var lp = start
        var rp = mid + 1
        // 进行合并
        while (lp <= mid && rp <= end) {
//            newArray.add(if (arr[lp] <= arr[rp]) arr[lp++] else arr[rp++])
            if (arr[lp] <= arr[rp]) {
                newArray.add(arr[lp])
                lp++
            } else {
                newArray.add(arr[rp])
                rp++
            }
        }
        // Error: 将剩余的元素copy到新数组
        while (lp <= mid) {
            newArray.add(arr[lp++])
        }
        while (rp <= end) {
            newArray.add(arr[rp++])
        }
        // copy回原数组
//        arr.addAll(0, newArray)
//        arr.addAll(start, newArray) // Error: 在start的位置插入新数组元素，导致原数组变长；最后造成堆栈溢出: java.lang.StackOverflowError
        for (i in newArray.indices) {
            arr[start + i] = newArray[i]
        }
    }
}