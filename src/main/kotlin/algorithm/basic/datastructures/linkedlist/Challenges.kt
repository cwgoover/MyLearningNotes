package algorithm.basic.datastructures.linkedlist

import algorithm.basic.datastructures.linkedlist.Challenges.addInReverse
import algorithm.basic.datastructures.linkedlist.Challenges.getMiddle
import algorithm.basic.datastructures.linkedlist.Challenges.isPalindrome
import algorithm.basic.datastructures.linkedlist.Challenges.isPalindromeByRecursive
import algorithm.basic.datastructures.linkedlist.Challenges.mergeSorted
import algorithm.basic.datastructures.linkedlist.Challenges.printInReverse
import algorithm.basic.datastructures.linkedlist.Challenges.printInReverseByStack
import java.util.Stack

/**
 * Created by i352072(erica.cao@sap.com) on 07/09/2022
 */
object Challenges {
    
    /**
     * ## Challenge 1: Reverse a linked list
     *
     * Create an extension function that prints out the elements of a linked list in reverse order.
     * Given a linked list, print the nodes in reverse order.
     */
    fun <T: Any> Mode.LinkedList<T>.printInReverse() {
        // The time complexity of this algorithm is O(n) since you have to traverse each node of the list.
        println("print in reverse:\n$this")
        // This function forwards the call to the recursive function that traverses the list,
        // node by node.
        this.nodeAt(0)?.printInReverse()
    }
    
    /**
     * A straightforward way to solve this problem is to use recursion.
     * Since recursion allows you to build a call stack, you need to call the print statements as
     * the call stack unwinds.
     */
    fun <T: Any> Mode.Node<T>.printInReverse() {
        /**
         * As you’d expect, this function calls itself on the next node.
         * The terminating condition is somewhat hidden in the null-safety operator.
         * If the value of next is null, the function stops because there’s no next node on which
         * to call printInReverse(). You’re almost done; the next step is printing the nodes.
         */
        this.next?.printInReverse()
        /**
         * Any code that comes after the recursive call is called only after the base case triggers
         * (i.e., after the recursive function hits the end of the list).
         */
        if (this.next != null) {
            print(" <- ")
        }
        print(this.value.toString())
    }
    
    fun <T: Any> Mode.LinkedList<T>.printInReverseByStack() {
        val stack = Stack<T>()
        for (item in this) {
            stack.push(item)
        }
        println("Print the linkedList in reverse order:")
        
        var node: T?
        while (stack.size > 0) {
            node = stack.pop()
            print("$node")
            
            if (stack.size > 0) {
                print(" -> ")
            }
        }
    }
    
    /**
     * ## Challenge 2: The item in the middle
     *
     * Given a linked list, find the middle node of the list.
     */
    fun <T: Any> Mode.LinkedList<T>.getMiddle(): T? {
        return getMiddleNode()?.value
    }
    
    private fun <T: Any> Mode.LinkedList<T>.getMiddleNode(): Mode.Node<T>? {
        /**
         * One solution is to have two references traverse down the nodes of the list where one is
         * twice as fast as the other. Once the faster reference reaches the end, the slower
         * reference will be in the middle.
         */
        var slow = this.nodeAt(0)
        var fast = this.nodeAt(0)
    
        /**
         * In the while declaration, you bind the next node to fast.
         * If there’s a next node, you update fast to the next node of fast, effectively stepping
         * down the list twice. slow is updated only once. This is also known as the runner technique.
         */
//        while (fast?.next != null) {
        while (fast != null) {
            fast = fast.next
            // fast每走两步，slow才走一步；这里判断fast.next 是否为空，就是保证slow是在fast走第二步的时候才移动!
            if (fast?.next != null) {
                slow = slow?.next
                fast = fast.next
            }
        }
        return slow
    }
    
    fun <T: Any> Mode.LinkedList<T>.getMiddleByErica(): T? {
        val middleSize = this.size / 2
        var node = nodeAt(0)
        for (s in 1..middleSize) {
            node = nodeAt(s)
        }
        return node?.value
    }
    
    /**
     * ## Challenge 3: Reverse a linked list
     *
     * To reverse a list is to manipulate the nodes so that the nodes of the list are linked
     * in the opposite direction.
     */
    fun <T: Any> Mode.LinkedList<T>.addInReverse(): Mode.LinkedList<T> {
        val newList = Mode.LinkedList<T>()
        this.nodeAt(0)?.addInReverse(newList)
        return newList
    }
    
    private fun <T: Any> Mode.Node<T>.addInReverse(list: Mode.LinkedList<T>) {
//        if (this.next != null) reversed()
        this.next?.addInReverse(list)
        list.add(this.value)
    }
    
    private fun <T: Any> reversedByErica(head: Mode.Node<T>?): Mode.Node<T>? {
        var preNode: Mode.Node<T>? = null
        var nextNode: Mode.Node<T>?
        var currentNode = head
    
        while (currentNode != null) {
            // store next node
            nextNode = currentNode.next
            // reverse pointer direction
            currentNode.next = preNode
            // shift the previous node & current node to next
            preNode = currentNode
            currentNode = nextNode
        }
        // Note that: here returns the new head node after reversing, rather than linkedList (=this).
        // Cause above actions doesn't change the head of linkedList, the original linkedList is outdated now.
        return preNode
    }
    
    /**
     * ## Challenge 4: Merging two linked lists
     *
     * Create a function that takes two sorted linked lists and merges them into a single sorted linked list.
     * Your goal is to return a new linked list that contains all the nodes from two lists
     * in sorted order. You may assume they are both lists are sorted in ascending order.
     *
     * This algorithm has a time complexity of O(m + n), where m is the # of nodes in the first list,
     * and n is the # of nodes in the second list.
     */
    fun <T: Comparable<T>> Mode.LinkedList<T>.mergeSorted(
        otherList: Mode.LinkedList<T>
    ): Mode.LinkedList<T> {
        // FIXME: need to check the corner case
        if (this.isEmpty()) return otherList
        if (otherList.isEmpty()) return this
        
        val result = Mode.LinkedList<T>()
        var node1 = otherList.nodeAt(0)
        var node2 = this.nodeAt(0)
    
        // The while loop continues until one of the lists reaches its end.
        while (node1 != null && node2 != null) {
            if (node1.value < node2.value) {
                node1 = append(result, node1)
            } else {
                node2 = append(result, node2)
            }
        }
    
        // Since this loop depends on both node1 and node2, it will terminate even if there are
        // nodes left in either list.
        while (node1 != null) {
            node1 = append(result, node1)
        }
    
        while (node2 != null) {
            node2 = append(result, node2)
        }
        
        return result
    }
    
    private fun <T: Comparable<T>> append(
        result: Mode.LinkedList<T>,
        node: Mode.Node<T>
    ): Mode.Node<T>? {
        result.append(node.value)
        return node.next
    }
    
    /**
     * ## Challenge 5: Palindrome Linked List
     *
     * Given the head of a singly linked list, return true if it is a palindrome or false otherwise.
     * Constraints:
     *  * The number of nodes in the list is in the range [1, 105].
     *  * 0 <= Node.val <= 9
     *
     *  Follow up: Could you do it in O(n) time and O(1) space?
     */
    
    /**
     * 使用快慢指针：
     * 避免使用 O(n) 额外空间的方法就是改变输入。
     *
     * 我们可以将链表的后半部分反转（修改链表结构），然后将前半部分和后半部分进行比较。比较完成后我们应该将链表恢复
     * 原样。虽然不需要恢复也能通过测试用例，但是使用该函数的人通常不希望链表结构被更改。
     *
     * 该方法虽然可以将空间复杂度降到 O(1)，但是在并发环境下，该方法也有缺点。在并发环境下，函数运行时需要锁定
     * 其他线程或进程对链表的访问，因为在函数执行过程中链表会被修改。
     *
     * 整个流程可以分为以下五个步骤：
     *  * 找到前半部分链表的尾节点。
     *  * 反转后半部分链表。
     *  * 判断是否回文。
     *  * 恢复链表。
     *  * 返回结果。
     *
     * 时间复杂度：O(n)，其中 n 指的是链表的大小
     * 空间复杂度：O(1)。我们只会修改原本链表中节点的指向，而在堆栈上的堆栈帧不超过 O(1)
     */
    fun <T: Any> Mode.LinkedList<T>.isPalindrome(): Boolean {
        val head = nodeAt(0) ?: return true
    
        // 找到前半部分链表的尾节点并反转后半部分链表
        val originMiddleNode = getMiddleNode()
        // FIXME: 为什么反转的head节点是 originMiddleNode?.next ?
        // * 若链表有奇数个节点，则low只能走到中间节点的前一个(因为fast最后一步只能走一个节点，所以low不会动，而停在前一个节点)
        //   那么这里的middle.next,就是奇数个节点中的中间那一个
        // * 若链表有偶数个节点，则low刚好走到中间节点；但是要反转的应该是后半段的节点，就是这里的middle.next节点开始
        // 这里使用 originMiddleNode?.next 做为reverse的头结点，是
//        val reversedMiddleNode = reversedByErica(originMiddleNode)
        val reversedMiddleNode = reversedByErica(originMiddleNode?.next)
    
        // 判断是否回文
        var p1: Mode.Node<T>? = head
        var p2 = reversedMiddleNode
        var result = true
        while (result && p2 != null) {
            if (p1?.value != p2.value) {
                result = false
            }
            p1 = p1?.next
            p2 = p2.next
        }
        
        // FIXME: 还原链表并返回结果
        originMiddleNode?.next = reversedByErica(reversedMiddleNode)
        return result
    }
    
    /**
     * 使用递归方法：(只有在链表长度比较小的时候才可行，否则会堆栈溢出！！)
     * 如果使用递归反向迭代节点，同时使用递归函数外的变量向前迭代，就可以判断链表是否为回文。
     * 算法的正确性在于递归处理节点的顺序是相反的（回顾上面打印的算法），而我们在函数外又记录了一个变量，
     * 因此从本质上，我们同时在正向和逆向迭代匹配。
     *
     * 时间复杂度：O(n)，其中 n 指的是链表的大小。
     * 空间复杂度：O(n)，其中 n 指的是链表的大小。我们要理解计算机如何运行递归函数，在一个函数中调用一个函数时，
     * 计算机需要在进入被调用函数之前跟踪它在当前函数中的位置（以及任何局部变量的值），通过运行时存放在堆栈中来实现
     * （堆栈帧）。在堆栈中存放好了数据后就可以进入被调用的函数。在完成被调用函数之后，他会弹出堆栈顶部元素，以恢复
     * 在进行函数调用之前所在的函数。在进行回文检查之前，递归函数将在堆栈中创建 n 个堆栈帧，计算机会逐个弹出进行处理。
     * 所以在使用递归时空间复杂度要考虑堆栈的使用情况。
     *
     * 这种方法不仅使用了 O(n) 的空间，且是比较差的做法，因为在许多语言中，堆栈帧的开销很大（如 Python），
     * 并且最大的运行时堆栈深度为 1000（可以增加，但是有可能导致底层解释程序内存出错）。
     * 为每个节点创建堆栈帧极大的限制了算法能够处理的最大链表大小。
     */
    fun <T: Any> Mode.LinkedList<T>.isPalindromeByRecursive(): Boolean {
        var currentNode: Mode.Node<T>? = nodeAt(0)
        
        // 其实这里面node一直到tail然后再反向一个一个跟currentNode从头到尾都对一遍才会结束；没办法做到
        // currentNode从前往后，node从后往前，然后两个到中间的位置停下比较；而是一直要移动到底，完全比较一边才行
        fun <T: Any> recursivelyCheck(node: Mode.Node<T>?): Boolean {
            if (node != null) {
                // FIXME: 这里挺精妙的，需要慢慢琢磨:
                // 如果在递归的退栈比对中(后面的和前面的节点作对比)发现不相等会返回false
                // 那么这里的判断就会为真，然后直接返回false，继续判断为真，返回false，直到递归完毕
                if (!recursivelyCheck(node.next)) {
                    return false
                }
                if (currentNode?.value != node.value) {
                    return false
                }
                currentNode = currentNode?.next
            }
            return true
        }
        return recursivelyCheck(nodeAt(0))
    }
    
    
    /**
     * 原设计没有用到链表的特点，而是通过自定义链表暴露的size，使用双指针法来比较两端的元素，并向中间移动；
     * 本质是按照数组的方式在实现，所以不可取; 但是我这里可以改一下，改成一种合格的算法，思路是一下：
     *  1. 复制链表值到数组列表中
     *  2. 使用双指针法判断是否为回文
     *
     * 时间复杂度：O(n)，其中 n 指的是链表的元素个数。
     *  * 第一步： 遍历链表并将值复制到数组中，O(n)。
     *  * 第二步：双指针判断是否为回文，执行了 O(n/2) 次的判断，即 O(n)。
     * 总的时间复杂度：O(2n) = O(n)。
     *
     * 空间复杂度：O(n)，其中 n 指的是链表的元素个数，我们使用了一个数组列表存放链表的元素值。
     */
    fun <T: Any> Mode.LinkedList<T>.isPalindromeByErica(): Boolean {
        val vals = mutableListOf<T>()
        
        // 将链表的值复制到数组中
        var currentNode = nodeAt(0)
        while (currentNode != null) {
            vals.add(currentNode.value)
            currentNode = currentNode.next
        }
    
        // 使用双指针判断是否回文
        var front = 0
        var end = vals.size - 1
        while (front < end) {
            if (vals[front] != vals[end]) return false
            front++
            end--
        }
        return true
        
        // ******** Erica: It's my implementation!!! *********
//        val mid = size / 2
//        for (i in 0 until mid) {
//            if (nodeAt(i)?.value != nodeAt(size - i - 1)?.value) {
//                return false
//            }
//        }
//        return true
    }

}

fun main() {
    val list = Mode.LinkedList<Int>()
    list.add(3)
    list.add(2)
    list.add(1)
    list.add(4)
    list.add(5)
    
    "printInReverse".run {
        list.printInReverse()
        println("\n")
    }
    
    "printInReverseByStack".run {
        list.printInReverseByStack()
        println("\n")
    }
    
    "getMiddle".run {
        println("Original: $list")
        println("The middle value is: ${list.getMiddle()}")
        println()
    }
    
    // WARNING: It caused the original linkedList is outdated, since it will affect other below cases, ignore here
//    "addInReverse".run {
//        println(list)
//        println("After reversing by erica:\n${list.reversedByErica()}")
//        println()
//    }
    
    "Reverse a linked list".run {
        println("Original: $list")
        println("After reversing a linked list:\n${list.addInReverse()}")
        println("\n")
    }
    
    "Merging two linked lists".run {
        val list1 = Mode.LinkedList<Int>()
        val list2 = Mode.LinkedList<Int>()
        list1.append(1).append(4).append(10).append(11)
        list2.append(-1).append(2).append(3).append(6)
        println("list1: $list1\nlist2: $list2")
        println("After merging two linked lists:\n${list1.mergeSorted(list2)}")
        println("\n")
    }
    
    "Palindrome Linked List".run {
        val list = Mode.LinkedList<Int>()
        list.append(3).append(2).append(1).append(9).append(9).append(1).append(2).append(3)
        println("Original: $list")
//        println("List is Palindrome? => ${list.isPalindrome()}")
        println("List is Palindrome? => ${list.isPalindromeByRecursive()}")
        println("\n")
    }
    
    "Palindrome Linked List: O(N)/O(1)".run {
        val list = Mode.LinkedList<Int>()
        list.append(3).append(2).append(1).append(9).append(9).append(1).append(2).append(3).append(6)
        println("Original: $list")
        println("Official: List is Palindrome? => ${list.isPalindrome()}")
        println("\n")
    }
}