package algorithm.basic.datastructures.trees_04

/**
 * Created by i352072(erica.cao@sap.com) on 11/14/2022
 */
typealias Visitor<T> = (TreeNode<T>) -> Unit

class TreeNode<T>(val value: T) {
    
    private val children: MutableList<TreeNode<T>> = mutableListOf()
    
    fun add(child: TreeNode<T>) = children.add(child)
    
    fun forEachDepthFirst(visit: Visitor<T>) {
        visit(this)
        children.forEach {
            it.forEachDepthFirst(visit)
        }
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
}