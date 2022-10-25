package algorithm.basic.datastructures.queues

import algorithm.basic.datastructures.stack.StackByLinkedListImpl

/**
 * Created by i352072(erica.cao@sap.com) on 09/20/2022
 */
object Mode {
    
    interface Queue<T: Any> {
        fun enqueue(element: T): Boolean
        fun dequeue(): T?
        
        val count: Int
            get
        val isEmpty: Boolean
            get() = count == 0
        
        fun peek(): T?
    }
    
    // Note that the interface implementation uses the same generic type T for the elements it stores.
    class ArrayListQueue<T: Any> : Queue<T> {
        private val list = arrayListOf<T>()
        
        override val count: Int
            get() = list.size
        
        override fun enqueue(element: T): Boolean {
            list.add(element)
            return true
        }
    
        override fun dequeue(): T? = if (isEmpty) null else list.removeAt(0)
    
        override fun peek(): T? = list.getOrNull(0)
        
        override fun toString(): String = list.toString()
    }
    
    /**
     * The LinkedList class in standard java library is Doubly-linked list implementation of
     * the List and Deque interfaces. All the operations perform as could be expected for a
     * doubly-linked list.
     *
     * But using standard LinkedList class to implement LinkedListQueue is too simple, so here
     * I use the DoublyLinkedList class implemented by myself to finish this class implementation.
     *
     */
    class LinkedListQueue<T: Any> : Queue<T> {
        
        private val list = DoublyLinkedList<T>()
        
        // FIXME: Since we can't directly get size info from DoublyLinkedList class, so we can use
        //  this variable to calculate size by myself!!
        private var size = 0
    
        override val count: Int
            get() = size
        
        override fun enqueue(element: T): Boolean {
            list.append(element)
            size++
            return true
        }
    
        override fun dequeue(): T? {
            // Implemented by Erica
//            if (isEmpty) {
//                return null
//            }
//            size--
//            return list.remove(list.first!!)
            
            // Reference version
            val firstNode = list.first ?: return null
            size--
            return list.remove(firstNode)
        }
    
        override fun peek(): T? = list.first?.value
    
        // For debugging purposes to override toString()
        // This leverages the DoublyLinkedList’s existing implementation for toString().
        override fun toString(): String = list.toString()
    }
    
    class RingBufferQueue<T: Any>(size: Int) : Queue<T> {
        
        private val ringBuffer: RingBuffer<T> = RingBuffer(size)
    
        override val count: Int
            get() = ringBuffer.count
    
        override fun peek(): T? =
            ringBuffer.first
        
        override fun enqueue(element: T): Boolean =
            ringBuffer.write(element)
    
        override fun dequeue(): T? =
            ringBuffer.read()
    
        // This code creates a string representation of the queue by delegating to the underlying ring buffer.
        override fun toString(): String =
            ringBuffer.toString()
    }
    
    class StackQueue<T: Any>: Queue<T> {
        private val leftStack = StackByLinkedListImpl<T>()
        private val rightStack = StackByLinkedListImpl<T>()
    
        override val count: Int
            get() = leftStack.count + rightStack.count
        
        override val isEmpty: Boolean
            get() = leftStack.isEmpty && rightStack.isEmpty
    
        override fun peek(): T? {
            if (leftStack.isEmpty) {
                transferElements()
            }
            return leftStack.peek()
        }
        
        override fun enqueue(element: T): Boolean {
            rightStack.push(element)
            return true
        }
    
        override fun dequeue(): T? {
            if (leftStack.isEmpty) {
                transferElements()
            }
            return leftStack.pop()
        }
    
        override fun toString(): String {
            return "Left stack: \n\t$leftStack \nRight stack: \n\t$rightStack"
        }
    
        private fun transferElements() {
            var element = rightStack.pop()
            while (element != null) {
                leftStack.push(element)
                element = rightStack.pop()
            }
        }
    }
    
}

fun main() {
    "Queue with ArrayList".run {
        val queue = Mode.ArrayListQueue<String>().apply {
            enqueue("Ray")
            enqueue("Brian")
            enqueue("Eric")
        }
        println("------- Queue with ArrayList -------")
        println(queue)
        queue.dequeue()
        println(queue)
        println("Next up: ${queue.peek()}")
        println()
    }
    
    "Queue with Doubly Linked List".run {
        val queue = Mode.LinkedListQueue<String>().apply {
            enqueue("Ray")
            enqueue("Brian")
            enqueue("Eric")
        }
        println("------- Queue with Doubly Linked List -------")
        println(queue)
        queue.dequeue()
        println(queue)
        println("Next up: ${queue.peek()}")
        println()
    }
    
    "Queue with Ring Buffer".run {
        val queue = Mode.RingBufferQueue<String>(10).apply {
            enqueue("Ray")
            enqueue("Brian")
            enqueue("Eric")
        }
        println("------- Queue with Ring Buffer -------")
        println(queue)
        queue.dequeue()
        println(queue)
        println("Next up: ${queue.peek()}")
        println()
    }
    
    "Queue with Double Stack".run {
        val queue = Mode.StackQueue<String>().apply {
            enqueue("Ray")
            enqueue("Brian")
            enqueue("Eric")
        }
        println("------- Queue with Double Stack -------")
        println(queue)
        queue.dequeue()
        println(queue)
        println("Next up: ${queue.peek()}")
        println()
    }
    
}