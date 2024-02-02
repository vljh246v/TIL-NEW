package com.demo.cookbook.ch03

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
