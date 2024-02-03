package com.demo.cookbook.ch03

import org.apache.commons.math3.complex.Complex

fun main() {
    val task = Task("twosome place", 2)
    println(task.name)
    println(task.priority)
}

// 3-1
class Task(val name: String, _priority: Int = DEFAULT_PRIORITY) {
    companion object {
        const val MIN_PRIORITY = 1
        const val MAX_PRIORITY = 5
        const val DEFAULT_PRIORITY = 3
    }

    var priority = validPriority(_priority)
        set(value) {
            field = validPriority(value)
        }

    private fun validPriority(p: Int) =
        p.coerceIn(MIN_PRIORITY, MAX_PRIORITY)
}


// 3-2, 3-3, 3-4
class Task2(val name: String) {
    var priority = 3
        set(value) {
            field = value.coerceIn(1..5)
        }

    val isLowPriority
        get() = priority < 3
}


data class Product(
    val name: String,
    val price: Double,
    val onSale: Boolean = false
)

// 3-7
data class OrderItem(
    val product: Product,
    val quantity: Int
)

// 3-10, 3-11
class Customer(val name: String) {
    /*
    private var _message: List<String>? = null

    val message: List<String>
        get() {
            if (_message == null) {
                _message = loadMessages()
            }

            return _message!!
        }
    */

    val message: List<String> by lazy { loadMessages() }

    private fun loadMessages(): MutableList<String> = mutableListOf(
        "Initial contact",
        "Convinced them to use Kotlin",
        "old training class. Sweet."
    ).also { println("Loaded message") }
}

// 3-13
data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)


// 3-14
operator fun Complex.plus(c: Complex) = this.add(c)
operator fun Complex.plus(d: Double) = this.add(d)
operator fun Complex.minus(c: Complex) = this.subtract(c)
operator fun Complex.minus(d: Double) = this.subtract(d)
operator fun Complex.div(c: Complex) = this.divide(c)
operator fun Complex.div(d: Double) = this.divide(d)
operator fun Complex.times(c: Complex) = this.multiply(c)
operator fun Complex.times(d: Double) = this.multiply(d)
operator fun Complex.times(i: Int) = this.multiply(i)
operator fun Double.times(c: Complex) = c.multiply(this)
operator fun Complex.unaryMinus() = this.negate()