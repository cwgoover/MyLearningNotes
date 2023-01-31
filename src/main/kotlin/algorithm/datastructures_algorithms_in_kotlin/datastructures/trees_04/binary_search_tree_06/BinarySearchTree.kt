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
    
    fun contains(value: T): Boolean {
        root ?: return false
        
        // In-order traversal has a time complexity of O(n), thus this implementation of contains
        // has the same time complexity as an exhaustive search through an unsorted array.
        // 这种方式是全量查找value，不会判断value的值从而选择不同的路径查找，所以时间复杂度是O(n)
        var found = false
        root?.traverseInOrder {
            if (it == value) found  = true
        }
        return found
    }
    
    fun containsPlus(value: T): Boolean {
        // Start by setting current to the root node.
        var current = root
        
        // While current is not null, check the current node’s value.
        while (current != null) {
            // If the value is equal to what you’re trying to find, return true.
            if (current.value == value) {
                return true
            }
    
            // Otherwise, decide whether you’re going to check the left or right child.
            current = if (value < current.value) {
                current.leftNode
            } else {
                current.rightNode
            }
        }
        return false
    }
    
    fun contains(subtree: BinarySearchTree<T>): Boolean {
        // Inside contains, you begin by inserting all of the elements of the current tree into a set.
        // 这里神来之笔啊，将tree上的数据直接转存到一个set里方便后面查找
        val set = mutableSetOf<T>()
        root?.traverseInOrder {
            set.add(it)
        }
        
        // isEqual will store the result. For every element in the subtree, you check if the value
        // is contained in the set. If at any point set.contains(it) evaluates to false, you’ll
        // make sure isEqual stays false even if subsequent elements evaluate to true by assigning
        // isEqual && list.contains(it) to itself.
        var isEqual = true
        subtree.root?.traverseInOrder {
            isEqual = isEqual && set.contains(it)
        }
        return isEqual
    }
    
    fun remove(value: T) {
        root = remove(root, value)
    }
    
    private fun remove(node: BinaryNode<T>?, value: T): BinaryNode<T>? {
        node ?: return null
        
        // when 用来找到要remove的node
        when {
            // 当找到要remove的node后，就要开始根据三种情况来分别处理
            value == node.value -> {
                // Case 1: remove Leaf node
                // In the case in which the node is a leaf node, you simply return null, thereby removing the current node.
                if (node.leftNode == null && node.rightNode == null) {
                    return null
                }
                // Case 2: remove Nodes with one child
                // If the node has no left child, you return node.rightChild to reconnect the right subtree.
                if (node.leftNode == null) {
                    return node.rightNode
                }
                // If the node has no right child, you return node.leftChild to reconnect the left subtree.
                if (node.rightNode == null) {
                    return node.leftNode
                }
                // Case 3: remove Nodes with two children
                // This is the case in which the node to be removed has both a left and right child.
                // You replace the node’s value with the smallest value from the right subtree.
                // You then call remove on the right child to remove this swapped value.
                node.rightNode?.min?.value?.let {
                    // 看对应的解释图，当发现要删除的node有两个子节点的时候；第一步，做替换，用右节点下最小的结点替换当前结点
                    // 相当于删除了当前结点
                    node.value = it
                }
                // 第二步，删除还在右节点下最小的那个结点，实际达到替代的目的
                node.rightNode = remove(node.rightNode, node.value)
            }
            value < node.value -> node.leftNode = remove(node.leftNode, value)
            else -> node.rightNode = remove(node.rightNode, value)
        }
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
        println("$bst\n")
    }
    
    val exampleTree = BinarySearchTree<Int>().apply {
        insert(3)
        insert(1)
        insert(4)
        insert(0)
        insert(2)
        insert(5)
    }
    
    "building a BST -- keep it from becoming unbalanced".run {
        println("${exampleTree}Much nicer!\n")
    }
    
    "finding a node".run {
        if (exampleTree.contains(5)) {
            println("Found 5!\n")
        } else {
            println("Couldn't find 5\n")
        }
    }
    
    "removing a node".run {
        println("Tree before removal:")
        println(exampleTree)
        exampleTree.remove(3)
        println("Tree after removing root:")
        println(exampleTree)
    }
    
}