package algorithm.datastructures_algorithms_in_kotlin.datastructures.stack_02

import algorithm.datastructures_algorithms_in_kotlin.datastructures.linkedlist_01.Mode
import algorithm.datastructures_algorithms_in_kotlin.datastructures.stack_02.Challenges.checkParentheses
import algorithm.datastructures_algorithms_in_kotlin.datastructures.stack_02.Challenges.printInReverseByStack

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
        // Create a new stack and start going through your string, character by character
        val stack = StackByArrayListImpl<Char>()
        for (c in this) {
            when (c) {
                // Push every opening parenthesis into the stack
                '(' -> stack.push(c)
                // FIXME: miss corner case: ')' is more than '('
//                ')' -> stack.pop()
                ')' -> if (stack.isEmpty) {
                    // Pop one item from the stack for every closing parenthesis, but if you’re
                    // out of items on the stack, your string is already imbalanced,
                    // so you can immediately return from the function
                    return false
                } else {
                    stack.pop()
                }
            }
        }
        return stack.isEmpty
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
    
    "check parentheses".run {
        val str1 = "h((e))llo(world)()"
        println("$str1 is balanced: ${str1.checkParentheses()}")
        val str2 = "(hello world"
        println("$str2 is balanced: ${str2.checkParentheses()}")
    }
}