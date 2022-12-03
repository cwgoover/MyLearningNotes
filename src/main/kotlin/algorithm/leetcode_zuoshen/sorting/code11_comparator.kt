package algorithm.leetcode_zuoshen.sorting

import java.util.PriorityQueue

/**
 * Created by i352072(erica.cao@sap.com) on 05/23/2022
 */
class code11_comparator {
    
    class IdAscendingComparator: Comparator<Student> {
        // 返回负数时，第一个参数排在前面
        // 返回正数时，第二个参数排在前面
        // 返回0时，谁在前面无所谓
        override fun compare(o1: Student, o2: Student): Int {
            // 上面比较策略最直观的写法
//            return if (o1.id < o2.id) {
//                -1    // 第一个参数的id小，返回负数；那就认为第一个参数放前面，即小就放在前面
//            } else if (o2.id < o1.id) {
//                1    // 第二个参数的id小，返回正数；那就认为第二个参数放前面，即小就放在前面
//            } else {
//                0
//            }
            
            // 如果o1的id小，减完就是负数，返回第一个参数o1
            // 如果o2的id小，减完就是正数，返回第二个参数o2
            // 如果两值相等，减完等于0
            return o1.id - o2.id
        }
    }
    
    class AgeDescendingComparator : Comparator<Student> {
        override fun compare(o1: Student, o2: Student): Int {
            // 如果o1的年龄小，减完就是正数，返回第二个参数o2，即返回大的那个
            // 如果o2的年龄小，减完就是负数，返回第一个参数o1，即返回大的那个
            // 如果两值相等，减完等于0
            return o2.age - o1.age
        }
    
    }
    
    class AComp: Comparator<Int> {
        // 返回负数时，并且如果是堆的话，相当于第一个参数放在上面
        // 返回正数时，并且如果是堆的话，相当于第二个参数放在上面
        // 返回0时，并且如果是堆的话，谁在上面都无所谓
        override fun compare(o1: Int, o2: Int): Int {
            // 按照大数的方式排序
            return o2 - o1
        }
    }
    
    // Note: In kotlin a nested class is static by default. To make it a non-static inner class it
    // needs to have an inner flag
    class Student(val name: String, val id: Int, var age: Int) {
        // Kotlin has a concise syntax for declaring properties and initializing them from the primary constructor, like above
        
        override fun toString(): String {
            return "Student #$id: name is $name, age is $age"
        }
    }
   
}

fun main() {
    val arr = arrayListOf(5, 4, 3, 2, 7, 9, 1, 0)
    // 默认从小到大的顺序
    arr.sort()
    println(arr)
    // 用AComp改成从大到小的顺序
    arr.sortWith(code11_comparator.AComp())
    println(arr)
    
    // 复杂数据，自己定义的类，可以自己定义一个比较器来实现类的排序；这样代码量会很少
    println("========================================")
    val stu1 = code11_comparator.Student("A", 2, 20)
    val stu2 = code11_comparator.Student("B", 3, 22)
    val stu3 = code11_comparator.Student("C", 1, 18)
    val students = arrayListOf(stu1, stu2, stu3)
    
    println("Student类，按照id升序排序:")
    // 如果不提供自定义的Comparator进行排序，默认会按照内存地址的大小来排序，是乱的，无意义的
    students.sortWith(code11_comparator.IdAscendingComparator())
    for (s in students) {
        println(s)
    }
    
    println("========================================")
    println("Student类，按照age降排序:")
    students.sortWith(code11_comparator.AgeDescendingComparator())
    for (s in students) {
        println(s)
    }
    
    println("========================================")
    println("========================================")
    val heap = PriorityQueue(code11_comparator.AComp())
    heap.add(6)
    heap.add(9)
    heap.add(3)
    heap.add(2)
    heap.add(10)
    
    println("大根堆的比较器:")
    while (heap.isNotEmpty()) {
        println(heap.remove())
    }
}