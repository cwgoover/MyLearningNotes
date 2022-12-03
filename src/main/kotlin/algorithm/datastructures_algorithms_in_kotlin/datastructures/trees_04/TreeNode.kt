package algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04

import algorithm.datastructures_algorithms_in_kotlin.datastructures.queues_03.Mode

/**
 * Created by i352072(erica.cao@sap.com) on 11/14/2022
 */
// you can visit the node and use the information into them
typealias Visitor<T> = (TreeNode<T>) -> Unit
/**
 * Each node is responsible for a value and holds references to all of its children using a mutable list
 */
class TreeNode<T>(val value: T) {
    // Here should be "private" in the common way
    val children: MutableList<TreeNode<T>> = mutableListOf()
    
    fun add(child: TreeNode<T>) = children.add(child)
    
    /**
     * Depth-first traversal:
     *
     * Depth-first traversal starts at the root node and explores the tree as far as possible
     * along each branch before reaching a leaf and then backtracking.
     */
    fun forEachDepthFirst(visit: Visitor<T>) {
        visit(this)
        children.forEach {
//            forEachDepthFirst(visit)  // FIXME: 如果这里没有特别指定对象it，那就是用的当前this为TreeNode<T>!
            it.forEachDepthFirst(visit)
        }
    }
    
    /**
     * Level-order traversal:
     *
     * Level-order traversal is a technique that visits each node of the tree based on the depth of the nodes.
     */
    fun forEachLevelOrder(visit: Visitor<T>) {
        visit(this)
        // Note how you use a queue to ensure that nodes are visited in the right level-order.
        val queue = Mode.ArrayListQueue<TreeNode<T>>()
        // You start visiting the current node and putting all its children into the queue.
        children.forEach { queue.enqueue(it) }
        
        // Then you start consuming the queue until it’s empty.
        var node = queue.dequeue()
        while (node != null) {
            // Every time you visit a node, you also put all its children into the queue.
            // This ensures that all nodes at the same level are visited one after the other.
            visit(node)
            node.children.forEach { queue.enqueue(it) }
            node = queue.dequeue()
        }
    }
    
    /**
     * Search:
     * You already have a method that iterates through the nodes, so building a search algorithm won’t take long
     */
    fun search(value: T): TreeNode<T>? {
        var result: TreeNode<T>? = null
        forEachLevelOrder {
            if (it.value == value) {
                result = it
            }
        }
        return result
    }
    
}

fun makeBeverageTree(): TreeNode<String> {
    val tree = TreeNode("Beverages")

    val hot = TreeNode("Hot")
    val cold = TreeNode("Cold")

    val tea = TreeNode("tea")
    val coffee = TreeNode("coffee")
    val chocolate = TreeNode("cocoa")

    val blackTea = TreeNode("black")
    val greenTea = TreeNode("green")
    val chaiTea = TreeNode("chai")

    val soda = TreeNode("soda")
    val milk = TreeNode("milk")

    val gingerAle = TreeNode("ginger ale")
    val bitterLemon = TreeNode("bitter lemon")

    tree.add(hot)
    tree.add(cold)

    hot.add(tea)
    hot.add(coffee)
    hot.add(chocolate)

    cold.add(soda)
    cold.add(milk)

    tea.add(blackTea)
    tea.add(greenTea)
    tea.add(chaiTea)

    soda.add(gingerAle)
    soda.add(bitterLemon)
    return tree
}

fun main() {
    val tree = makeBeverageTree()
    println("---------------------- forEachDepthFirst ----------------------------")
    tree.forEachDepthFirst {
        println(it.value)
    }
    
    println("\n---------------------- forEachLevelOrder ----------------------------")
    tree.forEachLevelOrder {
        println(it.value)
    }
    
    println("\n---------------------- search ----------------------------")
    tree.search("ginger ale")?.let {
        println("Found node: ${it.value}")
    }
    tree.search("WKD Blue")?.let {
        println("Found node: ${it.value}")
    } ?: println("Couldn't find WKD Blue")  // FIXME: 注意这里的 "?:" 用法!
}