package algorithm.basic.datastructures.queues_03

/**
 * Created by i352072(erica.cao@sap.com) on 09/21/2022
 */
class DoublyLinkedList<T: Any> {
    
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    
    val first: Node<T>?
        get() = head
    
    fun isEmpty(): Boolean = head == null
    
    fun append(value: T) {
        val node = Node(value = value, previous = tail)
        
        if (isEmpty()) {
            head = node
            tail = node
        } else {
            tail?.next = node
            tail = node
        }
    }
    
    // Refer to: Mode.LinkedList.nodeAt()
    fun node(index: Int): Node<T>? {
        var currentNode = head
        var currentIndex = 0
    
        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.next
            currentIndex++
        }
        return currentNode
    }
    
    fun remove(node: Node<T>): T {
        val prev = node.previous
        val next = node.next
    
        if (prev != null) {
            prev.next = next
        } else {
            head = next
        }
    
        if (next != null) {
            next.previous = prev
        } else {
            tail = node.previous
        }
        
        node.previous = null
        node.next = null
        return node.value
    }
    
    // FIXME: 晕了，双向链表根本不需要遍历就可以remove!
//    fun remove(node: Node<T>): T {
//        var currentNode = head
//
//        while (currentNode != null) {
//            if (currentNode.value == node.value) {
//                currentNode.previous?.next = currentNode.next
//                currentNode.next?.previous = currentNode.previous
//            }
//            currentNode = currentNode.next
//        }
//        return node.value
//    }
    
    override fun toString(): String {
        if (isEmpty()) {
            return "Empty list"
        }
        // print list from head node!!!
        return head.toString()
    }
    
    data class Node<T: Any> (
        var value: T,
        var next: Node<T>? = null,
        var previous: Node<T>? = null
    ) {
        override fun toString(): String {
            return if (next != null) {
                // Use recursive to print node!!
                "$value -> ${next.toString()}"
            } else {
                "$value"
            }
        }
    }
    
}

fun main() {
    val doubleLinkedList = DoublyLinkedList<Int>().apply {
        append(2)
        append(4)
        append(5)
        append(6)
        append(9)
    }
    println("Double linked list: $doubleLinkedList")
    println("#3 node is: ${doubleLinkedList.node(3)}")
    println("remove #3 node: ${doubleLinkedList.remove(doubleLinkedList.node(3)!!)}")
    println("Double linked list: $doubleLinkedList")
}