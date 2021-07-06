package kotlinlang

/**
 * https://kotlinlang.org/docs/nested-classes.html#inner-classes
 * Created by i352072(erica.cao@sap.com) on 07/06/2021
 */
class InnerClass {
    // A nested class marked as inner can access the members of its outer class.
    // Inner classes carry a reference to an object of an outer class:
    class Outer {
        private val bar: Int = 1
        inner class Inner {
            fun foo() = bar
        }
    }

    fun main() {
        val demo = Outer().Inner().foo()    // == 1
        println("demo=$demo")
    }
}

/**
 * Anonymous Inner Class
 *
 * Anonymous inner class instances are created using an object expression
 */
private class AnonymousInnerClass {

    class Window {
        fun addMouseListener(adapter: MouseAdapter) {
            println("addMouseListener")
        }
    }

    interface MouseAdapter {
        fun mouseClicked(e: String)
        fun mouseEntered(e: String)
    }

    fun main() {
        // Anonymous inner class instances are created using an object expression:
        Window().addMouseListener(object : MouseAdapter {
            override fun mouseClicked(e: String) {
                TODO("Not yet implemented")
            }

            override fun mouseEntered(e: String) {
                TODO("Not yet implemented")
            }
        })
    }
    /**
     * https://kotlinlang.org/docs/object-declarations.html#object-expressions
     * object expression:
     * Object expressions create objects of anonymous classes, that is, classes that aren't explicitly
     * declared with the class declaration. Such classes are handy for one-time use.
     * You can define them from scratch, inherit from existing classes, or implement interfaces.
     */
}

/**
 * Qualified this
 * https://kotlinlang.org/docs/this-expressions.html#qualified-this
 *
 * To access this from an outer scope (a class, extension function, or labeled function literal
 * with receiver) you write this@label, where @label is a label on the scope this is meant to be from:
 */
private class A {   // implicit label @A
    inner class B { // implicit label @B
        fun Int.foo() { // implicit label @foo
            val a = this@A  // A's this
            val b = this@B  // B's this

            val c = this    // foo's receiver, an Int
            val c1 = this@foo   // foo's receiver, an Int

            val funLit = lambda@ fun String.() {
                val d = this    // funLit's receiver
            }

            val funLit2 = { s: String ->
                // foo()'s receiver, since enclosing lambda expression
                // doesn't have any receiver
                val d1 = this
            }
        }
    }
}

fun main() {
    InnerClass().main()
    // Exception
    AnonymousInnerClass().main()
}