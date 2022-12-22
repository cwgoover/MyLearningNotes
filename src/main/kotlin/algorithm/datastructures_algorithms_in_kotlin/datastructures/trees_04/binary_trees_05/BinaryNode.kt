package algorithm.datastructures_algorithms_in_kotlin.datastructures.trees_04.binary_trees_05

/**
 * Created by i352072(erica.cao@sap.com) on 11/25/2022
 */
typealias Visitor<T> = (T) -> Unit

class BinaryNode<T: Any?>(var value: T) {
    
    var leftNode: BinaryNode<T>? = null
    var rightNode: BinaryNode<T>? = null
    
    // This recursive min property will help you find the minimum node in a subtree.
    val min: BinaryNode<T>
        get() = leftNode?.min ?: this
    
    override fun toString(): String {
        return diagram(this, "", "", "")
    }
    
    fun traverseInOrder(visit: Visitor<T>) {
        leftNode?.traverseInOrder(visit)
        visit(value)
        rightNode?.traverseInOrder(visit)
    }
    
    fun traversePreOrder(visit: Visitor<T>) {
        visit(value)
        leftNode?.traversePreOrder(visit)
        rightNode?.traversePreOrder(visit)
    }
    
    fun traversePostOrder(visit: Visitor<T>) {
        leftNode?.traversePostOrder(visit)
        rightNode?.traversePostOrder(visit)
        visit(value)
    }
    
    private fun diagram(
        node: BinaryNode<T>?,
        top: String = "",
        root: String = "",
        bottom: String = ""
    ): String {
        // TODO: tcao: need to understand!!!!
        return node?.let {
            if (node.leftNode == null && node.rightNode == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightNode, "$top ", "$top┌──", "$top│ ") +
                    root + "${node.value}\n" +
                    diagram(node.leftNode, "$bottom│ ", "$bottom└──", "$bottom ")
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
    
    seven.leftNode = one
    one.leftNode = zero
    one.rightNode = five
    seven.rightNode = nine
    nine.leftNode = eight
    return seven
}

fun main() {
    val tree = makeNumTree()
    println("\n$tree")
}