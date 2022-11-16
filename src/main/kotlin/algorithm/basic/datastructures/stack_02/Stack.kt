package algorithm.basic.datastructures.stack_02

import algorithm.basic.datastructures.linkedlist_01.Mode

/**
 * Created by i352072(erica.cao@sap.com) on 09/09/2022
 */
interface Stack<T: Any> {
    fun push(element: T)
    fun pop(): T?
    fun peek(): T?
    
    val count: Int
        get
    
    val isEmpty: Boolean
        get() = count == 0
}

// A variable number of arguments (also known as varargs) is a feature of the Kotlin language
// that allows you to pass a number of values as a single argument variable to a function.
// A vararg argument will be available inside the function as if it was an Array type data.
fun <T: Any> stackOf(vararg elements: T): Stack<T> {
    // Array.asList()
    return StackByArrayListImpl.create(elements.asList())
}

class StackByArrayListImpl<T: Any> : Stack<T> {
    private val storage = arrayListOf<T>()
    override val count: Int
        get() = storage.size
    
    companion object {
        fun <T: Any> create(items: Iterable<T>): Stack<T> {
            val stack = StackByArrayListImpl<T>()
            for (item in items) {
                stack.push(item)
            }
            return stack
        }
    }
    
    override fun push(element: T) {
        storage.add(element)
    }
    
    override fun pop(): T? {
        // FIXME: missing corner case check
//        if (storage.size == 0) {
//            return null
//        }
//        return storage.removeAt(storage.size - 1)
        
        if (isEmpty) return null
        return storage.removeAt(count - 1)
    }
    
    override fun peek(): T? {
//        if (storage.size == 0) {
//            return null
//        }
//        return storage[0]
        return storage.lastOrNull()
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
    
    override val count: Int
        get() = storage.size
    
    override fun push(element: T) {
        storage.push(element)
    }
    
    override fun pop(): T? {
        return storage.pop()
    }
    
    override fun peek(): T? {
        if (storage.isEmpty()) return null
        return storage.nodeAt(0)?.value
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
        println()
    }
    
    "initializing a stack from a list".run {
        val list = listOf("A", "B", "C", "D")
        val stack = StackByArrayListImpl.create(list)
        print(stack)
        println("Popped: ${stack.pop()}")
        println()
    }
    
    "initializing a stack from an array literal".run {
        val stack = stackOf(1.0, 2.0, 3.0, 4.0)
        print(stack)
        println("Popped: ${stack.pop()}")
    }
}