package algorithm.basic.datastructures

import algorithm.basic.datastructures.linkedlist.Mode

/**
 * Created by i352072(erica.cao@sap.com) on 09/09/2022
 */
interface Stack<T: Any> {
    fun push(element: T)
    fun pop(): T?
}

class StackByArrayListImpl<T: Any>: Stack<T> {
    private val storage = arrayListOf<T>()
    
    override fun push(element: T) {
        storage.add(element)
    }
    
    override fun pop(): T? {
        // FIXME: missing corner case check
        if (storage.size == 0) {
            return null
        }
        return storage.removeAt(storage.size - 1)
    }
    
    override fun toString(): String = buildString {
        appendLine("----------- top --------------")
        // If you just need the view of the list in reverse order, consider using the asReversed() function.
        storage.asReversed().forEach {
            appendLine("$it")
        }
        appendLine("------------------------------")
    }
}


class StackByLinkedListImpl<T : Any>: Stack<T> {
    private val storage: Mode.LinkedList<T> = Mode.LinkedList()
    
    override fun push(element: T) {
        storage.push(element)
    }
    
    override fun pop(): T? {
        return storage.pop()
    }
    
    override fun toString(): String {
        return storage.toString()
    }
}

fun main() {
    "using a stack".run {
        val stack = StackByArrayListImpl<Int>().apply {
            push(1)
            push(2)
            push(3)
            push(4)
        }
        print("Original stack:\n$stack")
        val poppedElement = stack.pop()
        if (poppedElement != null) {
            println("Popped: $poppedElement")
        }
        print("$stack")
        println()
    }
    
    "using a stack 2".run {
        val stack = StackByLinkedListImpl<Int>().apply {
            push(1)
            push(2)
            push(3)
            push(4)
        }
        println("Original stack: $stack")
        val poppedElement = stack.pop()
        if (poppedElement != null) {
            println("Popped: $poppedElement")
        }
    }
}