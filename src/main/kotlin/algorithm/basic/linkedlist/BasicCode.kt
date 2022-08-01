package algorithm.basic.linkedlist

/**
 * Created by i352072(erica.cao@sap.com) on 07/09/2022
 */
object BasicCode {


}

fun main() {
    val node1 = Mode.Node(value = 1)
    val node2 = Mode.Node(value = 2)
    val node3 = Mode.Node(value = 3)
    val node4 = Mode.Node(value = 4)
    val node5 = Mode.Node(value = 5)
    
    node1.next = node2
    node2.next = node3
    node3.next = node4
    node4.next = node5
    
    // You’ve just created five nodes
    println(node1)
}