package algorithm.basic.sorting

import algorithm.basic.sorting.code01_selectionSort.getRandomSet
import algorithm.basic.sorting.code2_bubbleSort.bubbleSort
import algorithm.basic.sorting.code2_bubbleSort.xorQuestionOneOdd
import algorithm.basic.sorting.code2_bubbleSort.xorQuestionTwoOdd

/**
 * 冒泡排序, 时间复杂度O(N^2)
 * 每次从第一位开始，如果左边的数比右边的数大，相邻两数交换；
 * 最后将数组中最大的数交换到最后一位。即通过相邻交换，把最大的数排到最尾部。
 * 然后再从第一个数开始找到次最大的数排在倒数第二个位置，以此类推。
 *
 * 冒泡排序就是最重的依次沉底
 *
 * Created by i352072(erica.cao@sap.com) on 01/15/2022
 */
object code2_bubbleSort {
    
    fun bubbleSort(arr: ArrayList<Int>?) {
        if (arr == null || arr.size < 2) {
            return
        }
        // 因为每次排序后，相对最后一位都被确认，所以循环范围是从后往前缩进
        for (n in arr.size - 1 downTo 0) {
            // 从第一位开始，如果左边的数比右边的数大，相邻两数交换
            for (i in 0 until n) {
                if (arr[i] > arr[i+1]) {
                    swap(arr, i, i+1)
                }
            }
        }
    }
    
    // Erica: 这个交换有问题的啊！！！！
    // FIXME: Error: 如果是交换相同位置的两个数，理论上应该是该数不变，但是用异或的方式交换后就会变成0！
    fun swap(arr: ArrayList<Int>, i: Int, j: Int) {
        // xor() 位运算，异或：无进位相加 (0+1=1, 1+1=0并且没有进位)
        
        // 异或特性:
        //  1. 0^N=N (0与任何数异或都是该数本身), N^N=0 (相同的数异或为0)
        //  2. 异或运算满足交换律和结合律: a^b=b^a, a^b^c=a^(b^c)
        //  3. 同样一组数异或的结果，和选择谁先谁后异或无关。同一组数异或结果不变
        //      (每个数都用2进制表示的话，一组数异或操作相当于异或每个数的对应2进
        //      制位。这样结果跟某一位上1的个数有关【奇数个1为1，偶数个1为0】，和顺序无关)
        
        // 假设 arr[i]=a, arr[j]=b
        arr[i] = arr[i].xor(arr[j]) // arr[i]=a^b, arr[j]=b
        arr[j] = arr[i].xor(arr[j]) // arr[i]=a^b, arr[j]=a^b^b=a^(b^b)=a^0=a
        arr[i] = arr[i].xor(arr[j]) // arr[i]=a^b^a=(a^a)^b=0^b=b, arr[j]=a
        // => arr[i]=b, arr[j]=a
        // 这种做法有个前提，是交换的两个值在内存里是独立的两个区域
        // 所以在数组中swap，必须保证i!=j。否则相当于同样内存区域在跟自己异或，会把该区域洗成零
    }
    
    /**
     * Question:
     *  在一个整形数组中，只有一种数出现了奇数次，其他所有数都出现偶数次；
     *      1. 怎么找到出现奇数次的数?
     *      2. 在数组中已知有两种数出现奇数次，其他所有数都出现偶数次；怎样找出这两组数？
     *  要求O(n)；只用有限几个变量，即额外空间复杂度O(1)
     */
    
    // Solution: 1. 怎么找到出现奇数次的数?
    fun xorQuestionOneOdd(arr: ArrayList<Int>) {
        println("The array is: $arr")
        // 数组中的所有元素逐一异或，得到的结果就是奇数次的数 (特性1,3)
        var oddNum = 0  // 0^N=N
        arr.forEach {
            oddNum = oddNum.xor(it)
        }
        println("=> The odd number in the array is: $oddNum")
    }
    
    // Solution: 2. 在数组中已知有两种数出现奇数次，其他所有数都出现偶数次；怎样找出这两个数？
    fun xorQuestionTwoOdd(arr: ArrayList<Int>) {
        // 假设两个奇数次的数分别为a,b
        var eor = 0  // 0^N=N
        arr.forEach {
            eor = eor.xor(it)
        }
        // => eor = a^b
        // eor != 0, 即必然二进制表示上有一位上是1
        
        // https://www.programiz.com/kotlin-programming/bitwise
        // inv() function inverts the bit pattern
        // 取反+1就是补码，eor&(~eor+1) = eor&(-eor)
        val oneBitInEor = eor.and(eor.inv() + 1)   // 提取出eor最右的一位1
        
        var oneOddValue = 0 // eor'
        for (cur in arr) {
            // 根据eor(a^b)最右的一位1将数组分为两堆，一堆的所有数字该位为1，另一堆的所有数字该位为0
            // 这样就会把这两个奇数通过该位分在不同的堆里，让每个堆里只有一个奇数存在
            if ((cur.and(oneBitInEor) == cur)) {
                // oneOddValue只跟该位上无1的一堆做异或运算，就可以找到两个奇数中的一个
                oneOddValue = oneOddValue.xor(cur)
            }
        }
        val anotherOddValue = eor.xor(oneOddValue)  // eor = a^b, oneOddValue = a
        println("oneOddValue=$oneOddValue, anotherOddValue=$anotherOddValue")
    }
}

fun main() {
    val set = getRandomSet(69, 20)
    val arr = set.toList() as ArrayList<Int>
    println("before sort: $arr")
    bubbleSort(arr)
    println("after sort: $arr")
    
    // For object
//        val cloneArr = arr.map { it.copy() }
    // It works only for immutable things like ints or strings
//        val arrCopy = arr.toArray()
    
    println("================================================")
    println("The question #1: Only one odd number in the array:")
    xorQuestionOneOdd(arrayListOf(1, 1, 3, 2, 4, 6, 2, 6, 4))
    
    println("The question #2: Two odd number in the array:")
    xorQuestionTwoOdd(arrayListOf(1, 1, 3, 2, 4, 6, 2, 6, 4, 9))
}