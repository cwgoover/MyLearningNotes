package algorithm.basic.datastructures.stack

import algorithm.basic.datastructures.linkedlist.Mode
import algorithm.basic.datastructures.stack.Challenges.printInReverseByStack

/**
 * Created by i352072(erica.cao@sap.com) on 09/14/2022
 */
object Challenges {
    
    fun <T: Any> Mode.LinkedList<T>.printInReverseByStack() {
        val stack = StackByArrayListImpl<T>()
        // Copy the content of the list into a stack, carefully putting the nodes on top of each other.
        for (item in this) {
            stack.push(item)
        }
        println("Print the linkedList in reverse order:")
        
        // Remove and print the nodes from the stack one by one, starting from the top.
        var node: T? = stack.pop()
        while (node != null) {
            print("$node")
            node = stack.pop()
            if (node != null) {
                print(" -> ")
            }
        }
    }
    
    fun String.checkParentheses(): Boolean {
    
    }
}

fun main() {
    "printInReverseByStack".run {
        val list = Mode.LinkedList<Int>()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)
        println("Original list:\n$list")
        list.printInReverseByStack()
        println("\n")
    }
}