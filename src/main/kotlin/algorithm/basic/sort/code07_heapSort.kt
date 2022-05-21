package algorithm.basic.sort

import algorithm.basic.sort.code01_selectionSort.swap

/**
 * 堆排序，时间复杂度O(N*logN), 额外空间复杂度O(1), 因为只用了有限的几个变量；
 *  作为对比：快排: 时间复杂度O(N*logN)，额外空间复杂度O(logN)
 *          归并: 时间复杂度O(N*logN), 额外空间复杂度O(N)
 *
 * 堆，在逻辑概念上是完全二叉树结构; 堆就是一种特殊的完全二叉树；分为大根堆，小根堆两种
 * 完全二叉树: 要么树是满二叉树，要么是从左到右依次变满的二叉树
 *
 * 假设数组连续一段的长度为size，堆在数组中的位置关系的描述如下:
 *  * index 位置的父节点位置: parent = (index - 1) / 2
 *  * index 位置的左孩子位置: leftChild = index * 2 + 1
 *  * index 位置的右孩子位置: rightChild = index * 2 + 2
 *
 *  堆结构最重要的两个操作就是：
 *    1. heapInsert: 在堆上新增加一个数，该数插入完全二叉树末尾然后上移的过程(即不断的与其父节点比较，若大就做交换，直到到头结点或者父值更大)
 *    2. heapify: 堆中从某个位置出发，将该数下沉到完全二叉树某个位置从而保持大根堆结构(在左右两个child中选出最大的
 *          那个然后比较，若小进行交换，直到均大于两个child或者没有左右child为止)
 *
 *  堆排序：
 *    1. 先将数组整体变成一个大根堆，建立堆的过程
 *          1.1 从上往下的方法(使用heapInsert)，时间复杂度为O(N*logN)
 *          1.2 从下往上的方法(使用heapify)，时间复杂度为O(N)
 *    2. 把堆中0位置的数跟末位置的数做交换，然后减少heapSize
 *    3. 用heapify()将0位置的数置换到堆中合适的位置，形成新的大根堆；时间复杂度为O(N*logN)
 *    4. 重复步骤2~3，直到heapSize减少到0时，排序完成
 *
 *  注：
 *    因为大根堆的头结点一定是堆中最大的值，所以每次将大根堆的头结点的值与堆末尾的值交换，然后减小堆的heapSize。这样
 *    类似于将堆头结点陆续出栈并按逆序从数组末尾开始排起。随着heapSize的减少，相当于将堆的最大值一个个吐出来插在堆尾部
 *    的位置从而达到数组排序，直到整个堆的值全部吐干净，即 heapSize=0 时，排序就可以结束了。
 *
 *  详细信息可以查看黄色笔记本📒
 *
 * Created by i352072(erica.cao@sap.com) on 05/05/2022
 */
object code07_heapSort {
    
    fun heapSort(arr: ArrayList<Int>?) {
        if (arr.isNullOrEmpty() || arr.size < 2) {
            return
        }
        
        // 将数组整体变成一个大根堆, 方法1：O(N*logN)
        for (i in 0 until arr.size) {   //  O(N)
            heapInsert(arr, i)      // O(logN)
        }
        
        // 将数组整体变成一个大根堆, 方法2：O(N)，优于方法1
//        for (i in arr.size - 1 downTo 0) {
//            // 注意这里heapSize为arr.size，因为节点一直往下做heapify，会与下面的节点做对比
//            heapify(arr, i, arr.size)
//        }
        
        // O(N*logN)
        var heapSize = arr.size
        while (heapSize > 0) {   // O(N)
            // 把堆中0位置的数跟末位置的数做交换，然后减少heapSize
            swap(arr, 0, heapSize - 1)   // O(1)
            heapSize--
            // 将0位置的数置换到堆中合适的位置，形成新的大根堆
            heapify(arr, 0, heapSize)    // O(logN)
        }
    }
    
    // 将数组的连续一段对应成大根堆
    private fun heapInsert(arr: ArrayList<Int>, index: Int) {
        var nIndex = index
        // Note: 新增的数不断与其父进行比较，如果发现值更大就交换，直到来到头结点或者父值比该值大就可以停止
        while (arr[nIndex] > arr[(nIndex - 1) / 2]) {
            // 这里while中的条件包含两种情况:
            // 1. index到达堆顶(树的头结点), 因为到顶后index = 0, (index-1)/2 = 0 => while(arr[0] > arr[0]) 不成立，停止循环
            // 2. index所在节点的值小于父节点的值，停止循环
            swap(arr, nIndex, (nIndex - 1) / 2)
            nIndex = (nIndex - 1) / 2   // 如大于父节点，交换后将坐标上移到新位置，下一个循环中继续跟父节点进行比较
        }
    }
    
    // 堆中从一个位置index出发往下移动的过程，叫做堆化heapify, index是指从哪个位置往下做heapify
    private fun heapify(arr: ArrayList<Int>, index: Int, heapSize: Int) {   // heapSize用来判断左右两个孩子是否越界
        var nIndex = index
        var leftChild = index * 2 + 1   // 左孩子下标
        while (leftChild < heapSize) {  // 下方还有孩子的时候; 因为左孩子下标总比右孩子下标小, 所以左孩子不越界说明一定有孩子, 此时右孩子可有可没有
            // 左右两个子节点选出最大的那个, 把下标给largest; 即如果有右孩子并且右孩子的值比左孩子大，下标为右孩子，否则是左孩子的下标
            // ERROR: miss (leftChild + 1 < heapSize)
            var largest = if ((leftChild + 1 < heapSize) && (arr[leftChild + 1] > arr[leftChild])) {
                leftChild + 1
            } else {
                leftChild   // 可能两种情况: 1. 没有右孩子; 2. 右孩子的值小于左孩子
            }
            // ERROR: 父节点和较大子节点作比较，谁的值大，谁把下标给largest; 这里父节点相当于nIndex的位置
            largest = if (arr[largest] > arr[nIndex]) largest else nIndex
            // ERROR: miss退出循环的条件
            if (largest == nIndex) {
                // 这里相当于父节点所在位置的值大于两个child，那么就可以停止循环了，这已经是一个大根堆结构了
                break
            }
            // 如果父节点的值小于某个child，交换两值；nIndex下移到被交换的子节点，作为下一个循环的父节点继续循环
            swap(arr, largest, nIndex)
            nIndex = largest    // nIndex下移
            leftChild = nIndex * 2 + 1  // 新左孩子是移动后的nIndex的左孩子
            
//            // 如果子节点大于父节点，交换两值；nIndex下移到被交换的子节点，继续下轮循环
//            if (arr[largest] > arr[nIndex]) {
//                swap(arr, largest, nIndex)
//                nIndex = largest
//                leftChild = nIndex * 2 + 1
//            }
        }
    }
    
}