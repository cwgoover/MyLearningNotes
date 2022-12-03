package algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04.binary_search_tree_06

import algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04.binary_trees_05.BinaryNode

/**
 * Created by i352072(erica.cao@sap.com) on 12/03/2022
 */
class BinarySearchTree<T: Comparable<T>> {
    var root: BinaryNode<T>? = null
    
    // The first insert is exposed to users, while the second will be used as a private helper method
    fun insert(value: T) {
        root = insert(root, value)
    }
    
    /**
     * Following the rules of the BST, nodes of the left child must contain values less than
     * the current node, whereas nodes of the right child must contain values greater than
     * or equal to the current node.
     */
    // 这里的两个参数相当于两种选择，要么选node返回回去，要么把value包装成新node返回回去
    // 下面结构体里就是操作如何做选择
    private fun insert(node: BinaryNode<T>?, value: T): BinaryNode<T> {
        // This is a recursive method, so it requires a base case for terminating recursion.
        // If the current node is null, you’ve found the insertion point and return the new BinaryNode.
        // 这里说明选择value返回的条件就是当node为空时返回
        node ?: return BinaryNode(value)    // FIXME: 注意这里的写法!!
        // This if statement controls which way the next insert call should traverse.
        // If the new value is less than the current value, you call insert on the left child.
        // If the new value is greater than or equal to the current value, you call insert on the right child.
        // 这里可以看成是选择何种路径来insert，即如果value小于当前node，就选择左子路径操作
        // 将left child传入insert，返回的node重新付给left child. 如果node.leftNode为空，就用value创建
        // 出的node；否则还是原来的left child
        if (value < node.value) {
            node.leftNode = insert(node.leftNode, value)
        } else {
            node.rightNode = insert(node.rightNode, value)
        }
        // Return the current node.
        // This makes assignments of the form node = insert(node, value) possible as insert will
        // either create node (if it was null) or return node (if it was not null).
        // 这里就是如果node不为空时保证传回去的还是原来的那个node
        return node
    }
    
    override fun toString(): String {
        return root?.toString() ?: "empty tree"
    }
    
}

fun main() {
    "building a BST".run {
        val bst = BinarySearchTree<Int>()
        (0..4).forEach {    // FIXME: 注意这里的写法!!
            bst.insert(it)
        }
        println(bst)
    }
}