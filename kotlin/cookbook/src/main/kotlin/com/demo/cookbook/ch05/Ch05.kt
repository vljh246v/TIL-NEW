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

    // 5-22
    val list = listOf("a", "b", "c", "d", "e", "f", "g")
    val (a, b, c, d, e) = list
    println("$a $b $c $d $e")

    // 5-25
    val sorted = golfers.sortedWith(
        compareBy({ it.score }, { it.last }, { it.first })
    )
    sorted.forEach { println(it) }

    golfers.sortedWith(comparator)
        .forEach { println(it) }
    golfers.sortedWith(comparatorDesc)
        .forEach { println(it) }

    // 5-30
    val team = Team("Warriors")
    team.addPlayers(Player("Curry"), Player("Thompson"), Player("Durant"), Player("Green"), Player("Cousins"))
    for (player in team.players) {
        println(player)
    }

    // 5-31
    for (player in team) {
        println(player)
    }
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

// 5-24
data class Golfer(
    val score: Int,
    val first: String,
    val last: String
)

val golfers = listOf(
    Golfer(70, "Jack", "Nicklaus"),
    Golfer(68, "Tom", "Watson"),
    Golfer(70, "Bubba", "Watson"),
    Golfer(70, "Tiger", "Woods"),
    Golfer(70, "Ty", "Weeb"),
)

val comparator = compareBy(Golfer::score)
    .thenBy(Golfer::last)
    .thenBy(Golfer::first)

val comparatorDesc = compareByDescending(Golfer::score)
    .thenByDescending(Golfer::last)
    .thenByDescending(Golfer::first)

// 5-29, 5-32
data class Player(val name: String)
class Team(
    val name: String,
    val players: MutableList<Player> = mutableListOf()
) : Iterable<Player> {
    fun addPlayers(vararg people: Player) = players.addAll(people)
    override fun iterator(): Iterator<Player> = players.iterator()
}

// 5-31
operator fun Team.iterator(): Iterator<Player> = players.iterator()