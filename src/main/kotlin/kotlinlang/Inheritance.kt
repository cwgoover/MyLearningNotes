package kotlinlang

/**
 * Created by i352072(erica.cao@sap.com) on 02/25/2021
 */
class Inheritance {

    /**
     * Derived class initialization order
     * https://kotlinlang.org/docs/inheritance.html#derived-class-initialization-order
     *
     * When designing a base class, you should therefore avoid using open members in the constructors,
     * property initializers, and init blocks.
     *
     * Constructors:
     * https://kotlinlang.org/docs/classes.html#constructors
     */
    open inner class Base(name: String) {
        init {
            println("Initializing a base class")
        }
        open val size: Int =
            name.length.also { println("Initializing size in the base class: $it") }
    }

    inner class Derived(
        name: String,
        lastName: String
    ) : Base(name.capitalize().also { println("Argument for the base class: $it") }) {
        init {
            println("Initializing a derived class")
        }
        override val size: Int =
            (super.size + lastName.length).also { println("Initializing size in the derived class: $it") }
    }

    /**
     * Overriding rules
     * https://kotlinlang.org/docs/inheritance.html#overriding-rules
     *
     * To denote the supertype from which the inherited implementation is taken, use super qualified
     * by the supertype name in angle brackets, e.g. super<Base>
     */
    open class Rectangle {
        open fun draw() { println("Drawing a rectangle") }
    }

    interface Polygon {

        fun draw() { println("Drawing a Polygon") } // interface members are 'open' by default
    }

    class Square : Rectangle(), Polygon {
        // The compiler requires draw() to be overridden:
        override fun draw() {
            super<Rectangle>.draw() // call to Rectangle.draw()
            super<Polygon>.draw() // call to Polygon.draw()
        }
    }

    /**
     * A member marked override is itself open, i.e. it may be overridden in subclasses.
     * If you want to prohibit re-overriding, use final:
     */
    class SubRect() : Rectangle() {
        final override fun draw() {
            super.draw()
        }
    }

    /**
     * Note that you can use the override keyword as part of the property declaration
     * in a primary constructor.
     */
    class SubRect2(override val vertexCount: Int = 4) : Shape   // Always has 4 vertices

    interface Shape {
        val vertexCount: Int
    }

    /**
     * If the derived class has a primary constructor, the base class can (and must) be initialized
     * right there, using the parameters of the primary constructor.
     *
     * If the derived class has no primary constructor, then each secondary constructor has to
     * initialize the base type using the super keyword, or to delegate to another constructor
     * which does that. Note that in this case different secondary constructors can call different
     * constructors of the base type:
     */
    class MyView : View {
        constructor(ctx: String) : super(ctx)
        constructor(ctx: String, attrs: String) : super(ctx, attrs)

    }

    open class View {
        constructor(ctx: String)
        constructor(ctx: String, attrs: String)
    }
}

fun main() {
    Inheritance().Derived("erica", "cao")
    println()
    Inheritance.Square().draw()
}