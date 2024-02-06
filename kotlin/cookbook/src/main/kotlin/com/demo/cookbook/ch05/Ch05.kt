package com.demo.cookbook.ch05

import java.util.*

class Ch05


// 5-2
val strings = arrayOf("this", "is", "an", "array", "of", "strings")

// 5-3
val nullStringArray = arrayOfNulls<String>(5)

// 5-4
val squares = Array(5) { i -> (i * i).toString() }

// 5-9
var numList = mutableListOf(3, 1, 4, 1, 5, 9)
var numSet = mutableSetOf(3, 1, 4, 1, 5, 9)
var map = mutableMapOf(1 to "one", 2 to "two", 3 to "three")

fun main() {
    // 5-13
    val keys = 'a'..'f'

    val map = keys.associateWith { char ->
        char to char.toString().repeat(5)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
    println(map)
}


// 5-14
data class Product(
    val name: String, var price: Double,
    var onSale: Boolean = false
)

// 5-15
fun onSaleProducts_ifEmptyCollection(products: List<Product>) =
    products.filter { it.onSale }
        .map { it.name }
        .ifEmpty { listOf("none") }
        .joinToString(separator = ", ")

fun onSaleProducts_ifEmptyString(products: List<Product>) =
    products.filter { it.onSale }
        .map { it.name }
        .joinToString(", ")
        .ifEmpty { "none" }