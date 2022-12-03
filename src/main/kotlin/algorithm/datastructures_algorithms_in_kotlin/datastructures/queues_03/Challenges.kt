package algorithm.datastructures_algorithms_in_kotlin.datastructures.queues_03

import algorithm.datastructures_algorithms_in_kotlin.datastructures.queues_03.Challenges.nextPlayer
import algorithm.datastructures_algorithms_in_kotlin.datastructures.queues_03.Challenges.reverse
import algorithm.datastructures_algorithms_in_kotlin.datastructures.stack_02.StackByArrayListImpl

/**
 * Created by i352072(erica.cao@sap.com) on 09/20/2022
 */
object Challenges {
    
    /**
     * ## Challenge 3: Monopoly
     *
     * Imagine you’re playing a game of Monopoly with your friends. The problem is that everyone
     * always forgets whose turn it is! Create a Monopoly organizer that tells you whose turn it is.
     * A great option is to create an extension function for Queue that always returns the next player.
     */
    fun  <T: Any> Mode.Queue<T>.nextPlayer(): T? {
        /**
         * Creating a board game manager is straightforward. Your primary concern is whose turn it is.
         * A queue data structure is a perfect choice to take care of game turns.
         */
        // Get the next player by calling dequeue. If the queue is empty, return null,
        // as the game has probably ended anyway.
        val person = this.dequeue() ?: return null
        // enqueue the same person, this puts the player at the end of the queue.
        this.enqueue(person)
        // Return the next player.
        return person
    }
    
    
    /**
     * ## Challenge 4: Reverse data
     *
     * Implement a method to reverse the contents of a queue using an extension function.
     * Hint: The Stack data structure has been included in the project.
     */
    fun <T: Any> Mode.Queue<T>.reverse() {
        val aux = StackByArrayListImpl<T>()
        
        // dequeue all the elements in the queue onto the stack.
        var element = this.dequeue()
        while (element != null) {
            aux.push(element)
            element = this.dequeue()
        }
        
        // pop all the elements off the stack and insert them into the queue.
        var next = aux.pop()
        while (next != null) {
            this.enqueue(next)
            next = aux.pop()
        }
    }
    
}

fun main() {
    "Board game manager with Queue".run {
        val queue = Mode.ArrayListQueue<String>().apply {
            enqueue("Vincent")
            enqueue("Remel")
            enqueue("Lukiih")
            enqueue("Allison")
        }
        println(queue)
    
        println("\t===== board game =======")
        queue.nextPlayer()
        println(queue)
        queue.nextPlayer()
        println(queue)
        queue.nextPlayer()
        println(queue)
        queue.nextPlayer()
        println(queue)
        println()
    }
    
    "Reverse queue".run {
        val queue = Mode.ArrayListQueue<String>().apply {
            enqueue("1")
            enqueue("21")
            enqueue("18")
            enqueue("42")
        }
        println("before: $queue")
        queue.reverse()
        println("after: $queue")
    }
}