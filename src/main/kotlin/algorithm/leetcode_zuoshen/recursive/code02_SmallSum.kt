package algorithm.leetcode_zuoshen.recursive

/**
 * 小和问题和逆序对问题
 * 1. 小和问题
 * 在一个数组中，每一个数左边比当前数小的数累加起来，就叫做这个数组的小和；求一个数组的小和
 * Sample:
 *  [1, 3, 4, 2, 5] 1左边比1小的数，没有；3左边比3小的数，1；4左边比4小的数，1、3；2左边比2小的数，1；5左边比5小的数，1、3、4、2
 *  => 小和 = 1 + 1 + 3 + 1 + 1 + 3 + 4 + 2 = 16
 *
 *  解题思路：
 *   核心：深度改写mergeSort，在mergeSort的过程中就可以依次求出整个数组所有的小和来；
 *
 *   因为小和问题可以换一种思路求解：
 *      求一个数左边比它小的数，等同于数组中计算一个数右边有多少个比该数大的数，就产生多少个该数的小和！(N * 该数值)
 *
 *   所以在mergeSort的merge过程中，一边将左侧的数与右侧的数进行排序，一边将左侧的数与右侧的数进行对比；左侧的数一位位
 *   跟右侧比过去，如果发现右侧某位上的数比左侧正在比较的数大，那么可以利用右侧是之前已经排好序的数组的特性，通过下标直接计算出个数，
 *   从而推算出左侧这个正在比较的数的小和(该数值 * 右侧比它大的数的个数 —— 个数通过下标计算)；
 *   这样就利用了之前的排序结果而省去了在对比过程中发现比该数大后继续对右侧数的遍历行为
 *
 *   整个merge过程中的对比行为，只需要关注左侧数组与右侧数组中的数的对比；至于两侧数组内的数的对比求小和；因为是在递归
 *   过程中最开始的叶节点一路计算上来的。到某一个节点时，左侧或右侧数组内的数已经通过之前的merge行为计算出了对应的小和值；
 *   所以最后的小和值就是将整个mergeSort中求出来的小和值相加，得到总的小和值
 *
 *   注：详细分解过程可以看笔记部分的图示
 *
 * Created by i352072(erica.cao@sap.com) on 04/02/2022
 */
object code02_SmallSum {
    
    fun smallSum(arr: ArrayList<Int>?): Int {
        if (arr == null || arr.size < 2) {
            return 0
        }
        return process(arr, 0, arr.size - 1)
    }
    
    // arr[L..R] 既要排好序，也要求小和
    private fun process(arr: ArrayList<Int>, L: Int, R: Int): Int {
        if (L == R) return 0    // [IMPORTANT]: 递归结束条件
        
        val mid = L + ((R - L) shr 1)
        // 这里很重要：相当于左侧排序求小和值 + 右侧排序求和小和值 + merge时产生小和的数量
        return process(arr, L, mid) + process(arr, mid + 1, R) + merge(arr, L, mid, R)
    }
    
    private fun merge(arr: ArrayList<Int>, L: Int, mid: Int, R: Int): Int {
        val newArray = ArrayList<Int>(R - L + 1)
        var lp = L
        var rp = mid + 1
        var smallSum = 0
        while (lp <= mid && rp <= R) {
            // [IMPORTANT]: 这里跟原来的mergeSort不一样，原来是"<="，两数相等时copy左侧数进新数组；但是这里不行，
            // 因为如果相等时copy左侧的数进新数组的话，就不知道右侧后面有多少个数比当前左侧的这个数大了
            // 原因是一旦copy这个数进新数组的话就相当于对这个数的操作结束了，下标会下移到下一个数进行比较、排序了
            if (arr[lp] < arr[rp]) {
                // 只有左侧比右侧小的时候才产生小和值，该小和值 = 当前左侧值 * 右侧比它大的数的个数 (R - rp + 1)
                smallSum += arr[lp] * (R - rp + 1)
                newArray.add(arr[lp++])
            } else {
                newArray.add(arr[rp++])
            }
        }
        while (lp <= mid) {
            newArray.add(arr[lp++])
        }
        while (rp <= R) {
            newArray.add(arr[rp++])
        }
        // copy newArray to arr to sort arr
        for (i in newArray.indices) {
            arr[L + i] = newArray[i]
        }
        return smallSum
    }
    
    /**
     * 2. 逆序对问题
     * 在一个数组中，如果左边的数比右边的数大，则这两个数构成一个逆序对；求所有逆序对的数量
     *
     * 假设有数组[3, 2, 4, 5, 0]，那么逆序对就是：
     * (3, 2), (3, 0), (2, 0), (4, 0), (5, 0) 共5对
     * 只要任何一个左边的数比右边的数大就可以组成一个逆序对，求逆序对数量；
     *
     * 该题与求小和是一样的题型：
     * * 求小和是右边有多少个数比左边的数大
     * * 逆序对是右边有多少个数比左边的数小
     */
}