package algorithm.basic.sort

import kotlin.random.Random

/**
 * Created by i352072(erica.cao@sap.com) on 03/27/2022
 */
object TruingTest {
    
    fun run(maxSize: Int, maxNum: Int, turingFun: (arr: List<Int>) -> Unit,
                comparator: (arr: List<Int>) -> Unit): Boolean {
        val arr1 = generateRandomArray(maxSize, maxNum)
        val arr2 = copyArray(arr1)
//        println(arr1)
        turingFun(arr1)
        comparator(arr2)
        if (!isEquals(arr1, arr2)) {
            println("turingFun: arr1 = $arr1")
            println("comparator: arr2 = $arr2")
            return false
        }
        return true
    }
    
    private fun generateRandomArray(maxSize: Int, maxNum: Int): ArrayList<Int> {
        // Java:
        // Random.nextInt() -> [0, 1) 所有的小数，等概率返回一个
        // Random.nextInt() * N -> [0, N) 所有的小数，等概率返回一个
        // (int)(Random.nextInt() * N) -> N -> [0, N) 所有的整数，等概率返回一个
    
        // Kotlin:
        // Random.nextFloat() -> [0, 1) 所有的小数，等概率返回一个
        val size: Int = ((maxSize + 1) * Random.nextFloat()).toInt() // 长度随机
        println("generateRandomArray: size = $size")
        val arr = ArrayList<Int>(size)
        for (i in 0 until size) {
            // 相减保证有负值
            arr.add(((maxNum + 1) * Random.nextFloat() - maxNum * Random.nextFloat()).toInt())
        }
        return arr
    }
    
    private fun copyArray(arr: List<Int>?): List<Int> {
        if (arr == null) return ArrayList()
        val newArray = ArrayList<Int>(arr.size)
        for (i in arr.indices) {
            newArray.add(arr[i])
        }
        return newArray
    }
    
    private fun isEquals(arr1: List<Int>?, arr2: List<Int>?): Boolean {
        if (arr1 == null && arr2 == null) return true
        if ((arr1 != null && arr2 == null) || (arr1 == null && arr2 != null)) return false
        if (arr1?.size != arr2?.size) return false
        
        for (i in arr1?.indices!!) {
            if (arr1[i] != arr2?.get(i)!!) {
                return false
            }
        }
        return true
    }
}

fun main() {

}