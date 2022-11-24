package algorithm.basic.datastructures.trees_04

import algorithm.basic.datastructures.queues_03.Mode
import algorithm.basic.datastructures.trees_04.Challenges.printEachLevel

/**
 * Created by i352072(erica.cao@sap.com) on 11/22/2022
 */
object Challenges {
    
    /**
     * ## Challenge 1: Tree challenge
     *
     * Print the values in a tree in an order based on their level. Nodes belonging to the same
     * level should be printed on the same line.
     */
    fun <T> TreeNode<T>.printEachLevel() {
        val queue = Mode.ArrayListQueue<TreeNode<T>>()
        // keep track of the number of nodes you’ll need to work on before you print a new line
        var nodesLeftInCurrentLevel = 0
        
        queue.enqueue(this)
    
        // Your level-order traversal continues until your queue is empty.
        while (queue.isEmpty.not()) {   // 这里queue代表下一行所有的子树叶
            nodesLeftInCurrentLevel = queue.count   // 这里开始准备处理这一行的结点了，所以将个数保存在变量方便做循环计数
    
            // 直到整个一行的结点都处理完毕了才会跳出循环
            while (nodesLeftInCurrentLevel > 0) {
                // 这里把每个结点dequeue出来显示，然后将它所有的子节点放在queue里。这样就算处理完这个结点了。
                // 处理完的结点就抛弃掉，这里就是减少保存个数的变量，相当于这行的结点少了一个
                val node = queue.dequeue()
                node?.let {
                    print("${node.value} ")
                    node.children.forEach { queue.enqueue(it) }
                    nodesLeftInCurrentLevel--
                } ?: break
            }
            // 打印换行符
            println()
        }
    }
}

fun makeTree(): TreeNode<Int> {
    // Build the sample tree shown in the diagram
    // Root of the tree
    val tree = TreeNode(15)
    
    // Second level
    val one = TreeNode(1)
    tree.add(one)
    
    val seventeen = TreeNode(17)
    tree.add(seventeen)
    
    val twenty = TreeNode(20)
    tree.add(twenty)
    
    // Third level
    val one2 = TreeNode(1)
    val five = TreeNode(5)
    val zero = TreeNode(0)
    one.add(one2)
    one.add(five)
    one.add(zero)
    
    val two = TreeNode(2)
    seventeen.add(two)
    
    val five2 = TreeNode(5)
    val seven = TreeNode(7)
    twenty.add(five2)
    twenty.add(seven)
    
    return tree
}

fun main() {
    val tree = makeTree()
    tree.printEachLevel()
}