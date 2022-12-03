package algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04.binary_trees_05

import kotlin.math.max

/**
 * Created by i352072(erica.cao@sap.com) on 11/27/2022
 */

/**
 * ## Challenge 1: The height of the tree
 *
 * Given a binary tree, find the height of the tree.
 * The height of the binary tree is determined by the distance between the root and the furthest leaf.
 * The height of a binary tree with a single node is zero since the single node is both the root and the furthest leaf.
 */
fun <T : Any> BinaryNode<T>.height(node: BinaryNode<T>? = this): Int {
    // 其实这里隐含了 node == null是返回 -1 的终止条件。递归一定要设置一个终止条件
    // 在这里用kotlin显示就会不太明显，要注意
    return node?.let {
        1 + max(height(node.leftNode), height(node.rightNode))
    } ?: -1   // 这里-1很重要! 当递归到最底下叶子时，因为该叶子再没有叶子了，所以node == null，返回-1.
              // 该叶子的左右叶子height都是-1，加1后，即最底层的叶子的height就是0。即从最顶层叶子的0开始往上加1计数.
}

/**
 * ## Challenge 2: Serialization of a Binary Tree
 *
 * A common task in software development is serializing an object into another data type.
 * This process is known as serialization, and it allows custom types to be used in systems that
 * only support a closed set of data types.
 *
 * An example of serialization is JSON. Your task is to devise a way to serialize a binary tree
 * into a list, and a way to deserialize the list back into the same binary tree.
 *
 * To clarify this problem, consider the following binary tree:
 *        15
 *   10      25
 * 5   12  17
 * A particular algorithm may output the serialization as
 * [15, 10, 5, null, null, 12, null, null, 25, 17, null, null, null].
 * The deserialization process should transform the list back into the same binary tree.
 */

// Traversal
fun <T> BinaryNode<T>.traversePreOrderWithNull(visit: Visitor<T?>) {
    visit(this.value)
    // FIXME: 这里不应该直接打印出来，毕竟是做serialization，应该交给visit去决定怎么做!
//    leftNode?.traversePreOrderWithNull(visit) ?: println("null")
//    rightNode?.traversePreOrderWithNull(visit) ?: println("null")
    
    // It’s critical to point out that you’ll need to also visit the null nodes since
    // it’s important to record those for serialization and deserialization.
    leftNode?.traversePreOrderWithNull(visit) ?: visit(null)
    rightNode?.traversePreOrderWithNull(visit) ?: visit(null)
}

// Serialization
fun <T> BinaryNode<T>.serialize(node: BinaryNode<T> = this): MutableList<T?> {
    val list = mutableListOf<T?>()
    node.traversePreOrderWithNull {
        list.add(it)
    }
    return list
}

// Deserialization
fun <T> BinaryNode<T>.deserialize(list: MutableList<T?>): BinaryNode<T>? {
    // This is the base case. If removeAt returns null, there are no more elements in the array,
    // thus you’ll end recursion here.
//    val rootValue = list.removeFirst() ?: return null
    
    // change it for deserializeOptimized(), in order to improve time complexity
    val rootValue = list.removeAt(list.size - 1) ?: return null
    
    // You reassemble the tree by creating a node from the current value and recursively calling
    // deserialize to assign nodes to the left and right children.
    // Notice this is very similar to the pre-order traversal, except, in this case, you’re
    // building nodes rather than extracting their values.
    val root = BinaryNode<T>(rootValue)
    root.leftNode = deserialize(list)
    root.rightNode = deserialize(list)
    return root
}

fun <T> BinaryNode<T>.deserializeOptimized(list: MutableList<T?>): BinaryNode<T>? {
    return deserialize(list.asReversed())
}

fun main() {
    val tree = makeNumTree()
    println(tree)
    println("The height of the tree is: ${tree.height()}")
    
    val array = tree.serialize()
    println("Output array: $array")
    println(tree.deserializeOptimized(array))
}