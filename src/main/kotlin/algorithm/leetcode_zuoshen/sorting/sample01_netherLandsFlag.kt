package algorithm.leetcode_zuoshen.sorting

import algorithm.leetcode_zuoshen.sorting.code01_selectionSort.getRandomSet
import algorithm.leetcode_zuoshen.sorting.code01_selectionSort.swap
import algorithm.leetcode_zuoshen.sorting.code05_netherLandsFlag.comparator
import algorithm.leetcode_zuoshen.sorting.code05_netherLandsFlag.netherLandsFlag
import algorithm.leetcode_zuoshen.sorting.code05_netherLandsFlag.netherLandsFlagQ1

/**
 *
 * Q1:
 *  给定一个数组arr和一个数num，请把小于等于num的数放在数组的左边，大于num的数放在数组的右边。
 *  要求额外空间复杂度O(1), 时间复杂度O(N)
 * A:
 *  1) 准备一个变量表示小于等于区域的右边界，一开始在数组的最左侧(0位的左面)。然后随着遍历数组的过程中根据以下规律扩大
 *  2) 当 arr[i] <= num 时，把当前数arr[i] 和小于等于区域的下一个数进行交换；然后将小于等于区域往右扩一个位置，当前数跳下一个(i++)
 *  3) 当 arr[i] > num 时，直接将i跳下一个(i++)
 *  4) 直到数组越界时停止
 * 注：
 *  这里核心的原理就是通过小于等于区边界将划分数组，保证小于等于区内的数组 <= num; 所以在遍历数组时一旦发现该数小于等于
 *  num时就把它添加到小于等于区(区域下一位与发现的当前数做交换)，从而相当于扩大小于等于区边界；这样遍历结束时，数组中小于区域外的剩下的数都是大于num
 *  注意这个问题是不需要排序的，只分左右
 *
 *
 * Q2 (荷兰国旗问题):
 *  给定一个数组arr和一个数num，请把小于num的数放在数组的左边，等于num的数放在数组的中间，大于num的数放在数组的右边。
 *  要求额外空间复杂度O(1), 时间复杂度O(N)
 *  注：类似荷兰国旗分三块，这里的分组内是不用排序的
 * A:
 *  1) 准备两个区域(两个变量)，一个是小于区域的右边界，可以想象为起始位置在数组的最左端(初始位置的左面)；第二个区域是
 *      大于区域的左边界，想象起始位置在数组的最右端(末尾位置的右面)；然后遍历数组：
 *  2) 当 arr[i] < num 时，把当前数arr[i] 和小于区域的下一个数进行交换；然后将小于区域往右扩一个位置，当前数跳下一个(i++)
 *  3) 当 arr[i] = num 时，直接将i跳下一个(i++)
 *  4) 当 arr[i] > num 时，把当前数arr[i] 和大于区域的前一个数进行交换；然后将大于区域往左扩一个位置，当前数位置不变(i不变)
 *  5) 当 i 大于或等于 大于区域左边界的时候，遍历停止 (因为大于区域内的数都是交换过来的数，类似于都遍历过了)
 * 注：
 *  1. 小于情况类似上面Q1的步骤的1的描述，交换是为了将小于num的数纳入小于区域内，所以才要和紧贴区域外围的数交换，并扩大区域
 *  2. 等于情况直接跳下一个其实相当于把等于的数留在中间区域了；随着小于区域的扩充，等于区域的数相当于被小于区域推着走:
 *      靠近小于区域边界的等于区域的数可能在遍历过程中被交换到其他位置，但是最后肯定都留在中间区域；因为小于区域和大于
 *      区域分别保证了区域内的数值都是小于、大于num的值，只要符合两区域的条件的数都会被换走，最后留下的只能是等于区域的数。
 *  3. 大于情况与小于情况有类似，就是要把当前数和紧贴大于区域外围的数交换，并扩大区域范围；但是不同的点时i不变；不变的原因
 *      是因为这里交换过来的数是从数组后端来的数，不像小于情况下交换的数，是还没有被遍历过的数。所以要停下来继续检查
 *      (遍历)这个数，以防遗漏
 *
 *
 * Created by i352072(erica.cao@sap.com) on 04/06/2022
 */
object code05_netherLandsFlag {
    
    fun netherLandsFlagQ1(arr: ArrayList<Int>?, num: Int) {
        if (arr == null || arr.size < 2) {
            return
        }
        var rightMargin = -1    // 表示小于等于区域的右边界，一开始在数组的最左侧(0位的左面)
        for (i in arr.indices) {
            if (arr[i] <= num) {
                swap(arr, rightMargin + 1, i)   // 交换两数
                rightMargin++  // 小于区域往右扩一个位置
                // 当前数跳下一个(i++)
            }
            // 大于情况不作处理
        }
    }
    
    fun netherLandsFlag(arr: ArrayList<Int>?, num: Int) {
        if (arr == null || arr.size < 2) {
            return
        }
        var rightMargin = -1    // 表示小于等于区域的右边界，一开始在数组的最左侧(0位的左面)
        var leftMargin = arr.size   //  大于区域的左边界，起始位置在数组的最右端(末尾位置的右面)
        var i = 0
        // FIXME: ERROR: TODO: why?
//        while (i <= leftMargin) {
        while (i < leftMargin) {
            if (arr[i] < num) {
                swap(arr, rightMargin + 1, i)
                rightMargin++
                i++
            } else if (arr[i] == num) {
                i++
            } else if (arr[i] > num) {
                swap(arr, i, leftMargin - 1)
                leftMargin--
            }
        }
    }
    
    /**
     *  # Erica's Q1 Implementation:
     *
     * 这里的逻辑是创建一个跟原数组同长度的数组，然后遍历原数组：如果当前数小于num就从新数组的头部顺序插入；如果当前数
     * 大于num就从新数组的尾部逆序插入；因为两数组一样长度，所以遍历完原数组后插入的新数组的元素个数是和原数组一致的。
     * 评:
     *  虽然这个算法的时间复杂度是O(N),但是空间复杂度不是O(1); 所以不符合题目要求，只能算一种笨拙的好理解的实现方式
     *
     */
    fun comparator(arr: ArrayList<Int>?, num: Int) {
        if (arr == null || arr.size < 2) {
            return
        }

//        val newArray = ArrayList<Int>(arr.size)
//        for (i in arr.indices) {
//            if (arr[i] <= m) {
//                newArray[i] = arr[i]
//            } else {
//                // FIXME: ERROR: 创建newArray的size只是开辟了size的空间大小
//                // newArray的真实size是数组存储数据的大小，这里不能反向操作。
//                newArray[arr.size - 1 - i] = arr[i]
//            }
//        }
    
        val newArray = IntArray(arr.size) {0}   // 创建一个实际长度为size，初始值全为0的新数组
        var lp = 0
        var rp = arr.size - 1
        for (i in arr.indices) {
            if (arr[i] <= num) {
                newArray[lp++] = arr[i]
            } else {
                newArray[rp--] = arr[i]
            }
        }
        // copy newArray to arr
        for (i in arr.indices) {
            arr[i] = newArray[i]
        }
    }
}

fun main() {
    while (true) {
        print("Write the num (If -1, exit): ")
        
        val num = Integer.valueOf(readLine())
        if (num == -1) {
            break
        }
        
        print("Write which method do you want to call (0:comparator, 1:netherLandsFlagQ1, 2:netherLandsFlag): ")
        val select = Integer.valueOf(readLine())
        
        println("\nStart sorting....")
        val set = getRandomSet(70, 20)
        val arr = set.toList() as ArrayList<Int>
        
        println("\nbefore sort: $arr")
        when (select) {
            0 -> {
                // Ref: https://kotlinlang.org/docs/reflection.html#function-references
                println("Run method: ${::comparator.name}, num= $num")
                comparator(arr, num)
            }
            1 -> {
                println("Run method: ${::netherLandsFlagQ1.name}, num= $num")
                netherLandsFlagQ1(arr, num)
            }
            2 -> {
                println("Run method: ${::netherLandsFlag.name}, num= $num")
                netherLandsFlag(arr, num)
            }
        }
        println("after sort: $arr")
        println("\nSort finished\n\n")
    }
    println("Exit now...")
}