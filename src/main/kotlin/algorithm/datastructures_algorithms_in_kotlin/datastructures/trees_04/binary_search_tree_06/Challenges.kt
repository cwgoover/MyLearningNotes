package algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04.binary_search_tree_06

/**
 * Created by i352072(erica.cao@sap.com) on 12/22/2022
 */
typealias Visitor<T> = (T) -> Unit

// use "BinaryNode<T: Comparable<T>>" to update the BinaryNode class declaration to make T type comparable
class BinaryNode<T: Comparable<T>>(var value: T) {
    
    var leftChild: BinaryNode<T>? = null
    var rightChild: BinaryNode<T>? = null
    
    val min: BinaryNode<T>?
        get() = leftChild?.min ?: this
    
    val isBinarySearchTree: Boolean
        get() = isBST(this, min = null, max = null)
    
    override fun toString() = diagram(this)
    
    // equals recursively checks two nodes and their descendants for equality.
    override fun equals(other: Any?): Boolean {
        // Here, you check the value of the left and right nodes for equality.
        // You also recursively check the left children and the right children for equality.
        return if (other != null && other is BinaryNode<*>) {
            this.value == other.value &&
                this.leftChild == other.leftChild &&
                this.rightChild == other.rightChild
        } else {
            false
        }
    }
    
    //  It needs to keep track of progress via a reference to a BinaryNode and also
    //  keep track of the min and max values to verify the BST property.
    private fun isBST(tree: BinaryNode<T>?, min: T?, max: T?): Boolean {
        // This is the base case. If tree is null, then there are no nodes to inspect.
        // A null node is a binary search tree, so you’ll return true in that case.
        // 这里是递归的终止条件，非常重要！！
        tree ?: return true     // FIXME: 注意写法，这里如果tree是空的话，直接做返回true的特殊处理
        
        // This is essentially a bounds check. If the current value exceeds the bounds of
        // the min and max values, the current node does not respect the binary search tree rules.
        // 这里是边界条件检查。即保证左节点小于等于父节点，右节点大于父节点
        if (min != null && tree.value <= min) {
            // 因为当前tree都是子节点，而min或者max要么是父节点，要么是父父节点，要么是默认值null；
            // 所以要按照二叉查找树的规律判断，一旦不符合规律就返回false
            // 这里min/max出现父父节点的情况，一般发生在左节点的右叉树上，或者右节点的左叉树上。具体的要仔细研究，逐步debug才能明白
            return false
        } else if (max != null && tree.value > max) {
            return false
        }
        
        // When traversing through the left children, the current value is passed in as the max value.
        // This is because nodes in the left side cannot be greater than the parent.
        // Vice versa, when traversing to the right, the min value is updated to the current value.
        // Nodes in the right side must be greater than the parent.
        // If any of the recursive calls evaluate false, the false value will propagate to the top.
        return isBST(tree.leftChild, min, tree.value) && isBST(tree.rightChild, tree.value, max)
    }
    
    private fun diagram(node: BinaryNode<T>?,
                        top: String = "",
                        root: String = "",
                        bottom: String = ""): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top│ ") +
                    root + "${node.value}\n" + diagram(node.leftChild, "$bottom│ ", "$bottom└──", "$bottom ")
            }
        } ?: "${root}null\n"
    }
}

fun makeNumTree(): BinaryNode<Int> {
    val zero = BinaryNode(0)
    val one = BinaryNode(1)
    val five = BinaryNode(5)
    val seven = BinaryNode(7)
    val eight = BinaryNode(8)
    val nine = BinaryNode(9)
    
    seven.leftChild = one
    one.leftChild = zero
    one.rightChild = five
    seven.rightChild = nine
    nine.leftChild = eight
    return seven
}

fun main() {
    val tree = makeNumTree()
    println("\n$tree")
    
    /**
     * ## Challenge 1: Is it a BST?
     *
     * Create a function that checks if a binary tree is a binary search tree.
     */
    println("isBST => ${tree.isBinarySearchTree}\n")
    
    
    /**
     * ## Challenge 2: Equality between BSTs
     *
     * Override equals() to check whether two binary search trees are equal.
     */
    val treeCopy = makeNumTree()
    println("tree == treeCopy?: ${tree == treeCopy}\n")
    
    /**
     * ## Challenge 3: BSTs with the same elements?
     *
     * Create a method that checks if the current tree contains all the elements of another tree.
     */
    val bst = BinarySearchTree<Int>()
    (0..6).forEach {
        bst.insert(it)
    }
    val bst2 = BinarySearchTree<Int>()
    (0..4).forEach {
        bst2.insert(it)
    }
    println("bst contains bst2?: ${bst.contains(bst2)}\n")
}