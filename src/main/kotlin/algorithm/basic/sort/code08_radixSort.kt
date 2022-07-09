package algorithm.basic.sort

import kotlin.math.max

/**
 * 基数排序 —— 时间复杂度O(N), 额外空间复杂度O(M)
 * 桶排序思想下的排序都是不基于比较的排序
 *
 * 注意：基数排序只能排正数的序，如果是数组中有负数，因为负数的符号位没有做处理，被当做正数处理，结果基本会错！！
 *
 * Created by i352072(erica.cao@sap.com) on 05/24/2022
 */
object code08_radixSort {
    
    fun radixSort(arr: ArrayList<Int>?) {
        if (arr == null || arr.size < 2) {
            return
        }
        radixSort(arr, 0, arr.size - 1, maxBits(arr))
    }
    
    // arr[begin..end] 排序, digit表示最大值有多少位
    private fun radixSort(arr: ArrayList<Int>, L: Int, R: Int, digit: Int) {
        val radix = 10  // 说明以十为基底
        // arr[]想在某个范围内[L, R]排序，就准备一个同等范围(长度)的数组，即准备的辅助空间的个数与原数组在 [L, R] 范围上等规模
        val bucket = Array(R - L + 1) { 0 }
        
        // 相当于arr数组中的每个数从个位开始，一直到最大数的最高位 一轮一轮的做入桶出桶的排序
        for (d in 1..digit) {
            // count就是表示词频表的数组；这里radix=10 (十进制) => 数组size=10, 相当于数组里每一个值的下标对应的是十进制的数字0~9,
            // 10个数字；值代表下标所代表的数字出现的次数(优化后代表arr数组中所有数某位上的数小于等于该数的个数); e.g:
            // count[0] 当前位(d位)是0的数字有多少个
            // count[1] 当前位(d位)是（0和1）的数字有多少个
            // count[2] 当前位(d位)是（0、1和2）的数字有多少个
            // count[i] 当前位(d位)是（0~i）的数字有多少个
            val count = Array(radix) { 0 }   // count[0..9]
            
            // 轮询一遍arr数组，先整理出数组中每个数对应d位置上的数出现的次数，记录在词频表count[]中
            for (i in L..R) {
                val j = getDigit(arr[i], d) // d=1时，取个位数字；d=2时，取十位数字；...
                count[j] += 1   // 记录该数字出现的次数(词频表)
            }
            
            // 优化词频表count[]，改为count[i]的值代表小于等于i代表的数出现的次数
            // 做法是将count数组中每一个值与它前一个值进行累加
            for (i in 1 until count.size) {
                count[i] = count[i - 1] + count[i]
            }
            
            // 从右往左遍历原数组arr，找出每个数在d位置上的值j，根据这个值j对应到count[j]得到该值出现的次数；
            // 假设count[j]=4，说明小于等于j的数有4个，而当前所在的这个数，因为是最右边的数，所以它应该在小于等于j
            // 这个区间(片)的最靠右位置，即这片的右边界，所以它应该在排好序的数组中针对d位置上小于等于j值的最后一位。
            // ==> 在这轮针对d位置上的排序中，它arr[i](当前值)应该在辅助数组bucket中针对d位置上j值的最后一位
            // 注意：当处理完这个数的时候，应该将它所在的词频表位置上的值减一；相当于抹掉它的存在
            for (i in R downTo L) {
                val j = getDigit(arr[i], d)     // 把对应位置的数字拿出来
                bucket[count[j] - 1] = arr[i]   // 根据count数组中的值再减一，填到辅助数组中去
                count[j] -= 1   // 然后单个位置上的词频减一
            }
            
            // 将暂时根据d位置上的数排好序的辅助数组bucket中的值全部copy回原数组arr，相当于维持了这次出桶的结果
            // 保存在arr里
            var i = L
            for (num in bucket) {
                arr[i++] = num
            }
            // 继续下个位置d，再出桶入桶；知道最高位也出桶入桶完毕，排序完成
        }
    }
    
    /**
     * 假设num是1633，d=3；也就是要取出第三位的数字，即数字6;
     * 做法是: 根据除法运算，将1633转换为16，然后再除以10并求余，这样就只剩下个位的数字6了
     * 将1633转换为16的做法是: 1633 / 100 = 1633 / 10^2 = 1633 / 10^(d - 1)
     */
    private fun getDigit(num: Int, d: Int): Int {
        // Math.pow(底数x, 几次方y); Math.pow(x,y)：求x的y次方，x，y的值都是浮点类型的
        // 这里就是求: 10^(d - 1)
        val power = Math.pow(10.0, (d - 1).toDouble()).toInt()
        // 这里就是 num / 10^(d - 1) 得到所求数在个位的数字，然后通过除以10的求余运算，得到个位数字
        return (num / power) % 10
    }
    
    private fun maxBits(arr: ArrayList<Int>): Int {
        var max = Int.MIN_VALUE     // 找到数组中的最大值，注意可能是负数
        for (item in arr) {
            max = max(max, item)
        }
        // 找出数组中最大的值有几个十进制位，即最大值有几位
        var res = 0
        while (max != 0) {
            res++
            max /= 10
        }
        return res
    }
    
}