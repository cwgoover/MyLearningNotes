package algorithm.basic.linkedlist

import algorithm.basic.linkedlist.Challenges.addInReverse
import algorithm.basic.linkedlist.Challenges.reversedByErica
import algorithm.basic.linkedlist.Challenges.getMiddle
import algorithm.basic.linkedlist.Challenges.printInReverse
import algorithm.basic.linkedlist.Challenges.printInReverseByStack
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
            if (fast?.next != null) {
                slow = slow?.next
                fast = fast.next
            }
        }
        return slow?.value
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
    
    fun <T: Any> Mode.LinkedList<T>.reversedByErica(): Mode.Node<T>? {
        var preNode: Mode.Node<T>? = null
        var nextNode: Mode.Node<T>? = null
        var currentNode = nodeAt(0)
    
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
     */
    fun mergeSorted() {
    
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
}