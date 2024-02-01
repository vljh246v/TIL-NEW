package com.demo.cookbook.ch02

fun main() {
    var name: String
    name = "Test"
    // name = null
}

class Person(
    val first: String,
    val middle: String?,
    val last: String
)

val jkRowling = Person("J", null, "Rowling")
val northWest = Person("North", null, "West")