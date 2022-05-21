package algorithm.basic.sort

import algorithm.utils.Log
import kotlin.random.Random

/**
 * Created by i352072(erica.cao@sap.com) on 03/27/2022
 */
object TruingTest {
    
    fun run(maxSize: Int, maxNum: Int, turingFun: (arr: ArrayList<Int>) -> Unit,
                comparator: (arr: ArrayList<Int>) -> Unit): Boolean {
//        val selfRefParams = ::run.parameters
        val arr1 = generateRandomArray(maxSize, maxNum)
        val arr2 = copyArray(arr1)
        println("r:   before arr = $arr1")
//        println("run turingFun: ${selfRefParams[3].name}")
        turingFun(arr1)
        println("r: >> after arr = $arr1")
//        println("run comparator: ${selfRefParams[4].name}")
        comparator(arr2)
        if (!isEquals(arr1, arr2)) {
            System.err.println("Error: the result is different:")
            Log.e("turingFun: arr1 = $arr1")
            Log.e("comparator: arr2 = $arr2")
            return false
        }
        Log.d("Run successfully!")
        return true
    }
    
    private fun generateRandomArray(maxSize: Int, maxNum: Int): ArrayList<Int> {
        // Java:
        // Random.nextInt() -> [0, 1) 所有的小数，等概率返回一个
        // Random.nextInt() * N -> [0, N) 所有的小数，等概率返回一个
        // (int)(Random.nextInt() * N) -> [0, N) 所有的整数，等概率返回一个
    
        // Kotlin:
        // Random.nextFloat() -> [0, 1) 所有的小数，等概率返回一个
        val size: Int = ((maxSize + 1) * Random.nextFloat()).toInt() // 长度随机
        val arr = ArrayList<Int>(size)
        for (i in 0 until size) {
            // 相减保证有负值
            arr.add(((maxNum + 1) * Random.nextFloat() - maxNum * Random.nextFloat()).toInt())
        }
        return arr
    }
    
    private fun copyArray(arr: ArrayList<Int>?): ArrayList<Int> {
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
//    val testTime = 10000
    val testTime = 60
    val maxSize = 50
    val maxNum = 120
    
    Log.i("## Start run test ($testTime):")
    
    for (i in 0 until testTime) {
        if (!TruingTest.run(maxSize, maxNum,
            { arr ->
                execTestMethod(arr)
            }, { arr ->
                // 对数器里的方法b，系统排序(无误)
                arr.sortBy { it }
            })) break
    }
}

fun execTestMethod(arr: ArrayList<Int>) {
//    code01_selectionSort.selectionSort(arr)
//    code03_insertSort.insertSort(arr)
//    code04_mergeSort.mergeSort(arr)
//    code06_quickSort.quickSort(arr)
    code07_heapSort.heapSort(arr)
}