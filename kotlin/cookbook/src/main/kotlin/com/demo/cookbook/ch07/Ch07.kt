package com.demo.cookbook.ch07

import java.util.*

// 7-3
fun main() {
    println(book)
}

fun createBook(): Book {
    return Book("BOOK")
}

data class Book(val name: String)

val book = createBook()
    .also { println(it) }


// 7-5
data class Site(
    val name: String,
    val latitude: Double,
    val longitude: Double
)


// 7-7
fun processString(str: String) {
    str.let {
        when {
            it.isEmpty() -> "Empty"
            it.isBlank() -> "Blank"
            else -> str.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
        }
    }
}

// 7-8
fun processNullableString(str: String?) =
    str?.let {
        when {
            it.isEmpty() -> "Empty"
            it.isBlank() -> "Blank"
            else -> str.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
        }
    } ?: "Null"