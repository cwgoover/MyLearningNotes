package algorithm.datastructures_algorithms_in_kotlin.datastructures.queues_03

/**
 * Created by i352072(erica.cao@sap.com) on 09/25/2022
 */
class RingBuffer<T: Any>(private val size: Int) {
    
    private var array = ArrayList<T?>(size)
    private var readIndex = 0
    private var writeIndex = 0
    
    val count: Int get() = availableSpaceForReading
    val isEmpty: Boolean get() = (count == 0)
    val isFull: Boolean get() = availableSpaceForWriting == 0
    
//    val first: T? get() = array[readIndex]
    val first: T? get() = array.getOrNull(readIndex)
    
    private val availableSpaceForReading: Int
        get() = writeIndex - readIndex
    // FIXME: 这里的思想很重要! 如果availableSpaceForReading == size，说明全部空间写满了，没有地方可以写了
    private val availableSpaceForWriting: Int
        get() = size - availableSpaceForReading
    
    // FIXME: 这里不能这样写，writeIndex 代表写入的元素个数，readIndex代表读取的元素个数；所以他们俩不管
    //  array的长度，只记录个数！
//    fun write_byErica(element: T): Boolean {
//        if (isFull) return false
//        if (writeIndex > size) {
//            writeIndex = 0
//            array[writeIndex] = element
//        } else {
//            array[writeIndex] = element
//        }
//        writeIndex++
//        return true
//    }
    
    fun write(element: T): Boolean {
        return if (!isFull) {
            // 如果当前数组还没有满的话，直接添加
            if (array.size < size) {
                array.add(element)
            } else {
                array[writeIndex % size] = element
            }
            writeIndex++
            true
        } else {
            false
        }
    }
    
    fun read(): T? {
        return if (!isEmpty) {
            val element = array[readIndex % size]
            readIndex++
            element
        } else {
            null
        }
    }
    
    override fun toString(): String {
        val values = (0 until availableSpaceForReading).map { offset ->
            "${array[(readIndex + offset) % size]}"
        }
        return values.joinToString(prefix = "[", separator = ", ", postfix = "]")
    }
    
}