package algorithm.basic.recursive

/**
 * Created by i352072(erica.cao@sap.com) on 03/28/2022
 */
class code01_GetMax {
    
    companion object {
        fun getMax(arr: ArrayList<Int>): Int {
            return code01_GetMax().process(arr, 0, arr.size - 1)
        }
    }
    
    // arr[L..R]范围上求最大值     N
    fun process(arr: ArrayList<Int>, L: Int, R: Int): Int {
        if (L == R) {   // arr[L..R] 范围上只有一个数，直接返回， base case (终止条件)
            return arr[L]
        }
        
        //  mid = (L + R) / 2;          普通算法，这里(L + R)得到的值可能会移除，所以代码里不这样写
        //  mid = L + (R - L) / 2;      因为R, L, (R-L), (R-L)/2 都不可能溢出
        //  mid = L + (R - L) >> 1;     优化算法，(R-L)向右移1位，相当于除2；但是比除2快
        val mid = L + ((R - L) shr 1)  // 中点
        val leftMax = process(arr, L, mid)
        val rightMax = process(arr, mid + 1, R)
        // Return this value if it's greater than or equal to the minimumValue or the minimumValue otherwise
        return leftMax.coerceAtLeast(rightMax)  // Java: Math.max()
    }
}