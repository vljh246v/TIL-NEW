package com.demo.cookbook.ch02

fun main() {

    var name: String
    name = "Test"
    // name = null


    // 2-2
    val jkRowling = Person("J", null, "Rowling")
    val northWest = Person("North", null, "West")

    // 2-3
    val p1 = Person("North", null, "West")
    if (p1.middle != null) {
        val p1MiddleNameLength = p1.middle.length
    }

    // 2-4
    var p2 = Person("North", null, "West")
    if (p2.middle != null) {
        val p2MiddleNameLength = p2.middle!!.length
    }

    // 2-5
    var p3 = Person("North", null, "West")
    val p3MiddleNameLength = p3.middle?.length

    // 2-6
    var p4 = Person("North", null, "West")
    val p4MiddleNameLength = p4.middle?.length ?: 0
}

class Person(
    val first: String,
    val middle: String?,
    val last: String
)

