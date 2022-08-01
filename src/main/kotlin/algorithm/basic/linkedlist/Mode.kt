package algorithm.basic.linkedlist

/**
 * Created by i352072(erica.cao@sap.com) on 07/13/2022
 */
object Mode {
    
    // Note: Using "V : Any" to set an upper bound for the type parameter (类型参数的上限) ensures
    // that V will always be a non-nullable type.
    data class Node<V: Any>(var value: V, var next: Node<V>? = null) {
        override fun toString(): String {
            return if (next != null) {
                "$value -> ${next.toString()}"  // 这里类似递归操作，打印了当前Node之后链接的所有Node的value
            } else {
                "$value"
            }
        }
    }
    
    class LinkedList<T: Any> {
        private var head: Node<T>? = null
        private var tail: Node<T>? = null
        private var size: Int = 0
    
        /** Adds a value at the front of the list. This is also known as head-first insertion. */
        fun push(value: T): LinkedList<T> {
//            val node = Node(value = value)
//            node.next = head
//            head = node
            head = Node(value = value, next = head)
    
            // In the case in which you’re pushing into an empty list, the new node is both
            // the head and tail of the list.
            if (tail == null) {
                tail = head
            }
            
            // Since the list now has a new node, you increment the value of size.
            size++
    
            // add return type: You’ll use the fluent interface pattern to chain multiple push calls.
            return this
        }
        
         /*
          * Go back to push() and add LinkedList<T> as its return type.
          * Then, wrap the code of the method in an "apply" call to return the list that
          * you’ve just pushed an element into.
          */
        fun _push_fluent_interfact_type(value: T) = apply {
            head = Node(value = value, next = head)
            if (tail == null) tail = head
            size++
        }
    
        /** Adds a value at the end of the list, which is known as *tail-end insertion*. */
        fun append(value: T): LinkedList<T> {
            // FIXME: ERROR: miss掉这里的判空逻辑，下面的tail就一路是null，导致最后没有Node加到LinkedList里
            /**
             * Like before, if the list is empty, you’ll need to update both head and tail to
             * the new node. Since append on an empty list is functionally identical to push,
             * you invoke push to do the work for you.
             */
            if (isEmpty()) {
                // 使用push操作，直接处理好了head和tail的指向
                push(value)
                return this
            }
    
            /**
             * In all other cases, you create a new node after the current tail node.
             * tail will never be null here because you’ve already handled the case where
             * the list is empty in the if statement.
             */
            tail?.next = Node(value = value, next = null)
            // Since this is tail-end insertion, your new node is also the tail of the list.
            tail = tail?.next
            
            // FIXME: ERROR: 如果之前没有判断第一次添加数据的情况而先进行处理，这里的tail也是null！
            //        所以处理第一次append数据时，不能采用这种方法 —— 无效
//            if (head == null) {
//                head = tail
//            }
            
            // Since this is tail-end insertion, your new node is also the tail of the list.
            // You also have to increment size since a new value was added to the list.
            size++
            return this
        }
    
        /** Adds a value after a particular node of the list. */
        fun insert(value: T, afterNode: Node<T>): Node<T> {
            // FIXME: MISS: In the case where this method is called with the tail node, you call the
            // functionally equivalent append method. **This takes care of updating tail**
            if (tail == afterNode) {
                append(value)
                return tail!!
            }
            
            val newNode = Node(value = value, next = afterNode.next)
            afterNode.next = newNode
            size++
            return newNode
        }
        
        /** tries to retrieve a node in the list based on the given index */
        fun nodeAt(index: Int): Node<T>? {
            var currentNode = head
            var currentIndex = 0
    
            // 这里还是遍历链表找到index所在的node, "currentNode == null" 代表到了tail.next, 或者本身链表为空
            // "currentIndex < index"把控查找的个数，不等于index是如果已经到了index前一个位置的话，下面被执行的
            // currentNode.next就是index要找的node
            while (currentNode != null && currentIndex < index) {
                // Using a while loop, you move the reference down the list until you reach the desired
                // index. Empty lists or out-of-bounds indexes will result in a null return value.
                currentNode = currentNode.next
                currentIndex++
            }
            return currentNode
        }
        
        // Erica: awful code!!!
        /*fun nodeAt(index: Int): Node<T>? {
            if (size == 0 || index < 0 || index >= size) return null

            var node: Node<T>? = head
            for (i in 0 until index) {
                node = node?.next
            }
            return node
        }*/
        
         /*
          * Removing a value at the front of the list is often referred to as pop. Returns the value
          * that was removed from the list. This value is nullable since it’s possible that the list
          * is empty.
          */
        fun pop(): T? {
            if (isEmpty()) return null
            
            val result = head?.value
            head = head?.next
            // If the list becomes empty, you set tail to null as well.
            if (isEmpty()) tail = null
            
            size--
            return result
        }
        
        fun removeLast(): T? {
            // If head is null, there’s nothing to remove, so you return null
            val head = head ?: return null
            // If the list only consists of one node, removeLast is functionally equivalent to pop
            if (head.next == null) return pop()
            // At this point, you know that you’ll be removing a node, so you update the size of the list accordingly.
            size--
            
            // You keep searching for the next node until current.next is null. This signifies that
            // current is the last node of the list.
            var prev = head
            var current = head
            var next = current.next
            while (next != null) {
                prev = current
                current = next
                next = current.next
            }
            // Since current is the last node, you disconnect it using the prev.next reference. You also make sure to update the tail reference.
            prev.next = null
            tail = prev
            return current.value
        }
        
        fun removeLast_by_Erica(): T? {
            if (isEmpty()) return null
            
            var currentNode = head
            if (currentNode == tail) {
//                val value = currentNode?.value
//                head = null
//                tail = null
//                return value
                
                // Since pop will handle updating the head and tail references, you can delegate
                // this work to the pop function.
                return pop()
            }
            while (currentNode?.next != tail) {
                currentNode = currentNode?.next
            }
            val value = tail?.value
//            tail = null // FIXME: ERROR!! tail应该指向前一个node
            tail = currentNode
            currentNode?.next = null
            size--
            return value
        }
        
        fun removeAfter(node: Node<T>): T? {
            // FIXME: ERROR: 如果node就是最后一个，即tail。那么remove它后面的相当于不做操作
//            if (node.next == null) {
//                return removeLast()
//            }
            if (node.next != null) {
                size--
            }
            
            // FIXME: MISS: special case
            if (node.next == tail) {
                // Special care needs to be taken if the removed node is the tail node
                // since the tail reference will need to be updated.
                tail = node
            }
            
            val afterNode = node.next
            node.next = afterNode?.next
            return afterNode?.value
        }
        
        fun isEmpty(): Boolean = size == 0
    
        override fun toString(): String {
            return if (isEmpty()) {
                "Empty list"
            } else {
                head.toString()
            }
        }
    }
    
}

fun main() {
    // fluent interface push
    val list = Mode.LinkedList<Int>()
    list.push(3).push(2).push(1)
    println("fluent push list: $list")
    // ---Example of push---
    // 1 -> 2 -> 3
    
    list.append(4)
    println("After append: $list")
    // ---Example of append---
    // 1 -> 2 -> 3 -> 4
    
    var middleNode = list.nodeAt(1)!!
    for (i in 1..3) {
        middleNode = list.insert(-1 * i, middleNode)
    }
    println("After inserting: newNode = $middleNode")
    println("After inserting: $list")
    // ---Example of inserting at a particular index---
    // After inserting: newNode = -3 -> 3 -> 4
    // After inserting: 1 -> 2 -> -1 -> -2 -> -3 -> 3 -> 4
    println()
    
    val popedValue = list.pop()
    println("After popping list: $list")
    println("Popped value: $popedValue")
    // After popping list: 2 -> -1 -> -2 -> -3 -> 3 -> 4
    // Popped value: 1
    
    list.append(10)
    println("After append: $list")
    val removedValue = list.removeLast()
    println("After remove last: $list")
    println("Removed value: $removedValue")
    // ---Example of removing the last node---
    // After append: 2 -> -1 -> -2 -> -3 -> 3 -> 4 -> 10
    // After remove last: 2 -> -1 -> -2 -> -3 -> 3 -> 4
    // Removed value: 10
    
    val index = 1
    val node = list.nodeAt(index - 1)!!
    val removedValue2 = list.removeAfter(node)
    println("After removing at index $index: $list")
    println("Removed value: $removedValue2")
    // ---Example of removing a node after a particular node---
    // After removing at index 1: 2 -> -2 -> -3 -> 3 -> 4
    // Removed value: -1
}