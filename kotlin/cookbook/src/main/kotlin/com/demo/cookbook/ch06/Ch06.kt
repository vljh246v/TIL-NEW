package com.demo.cookbook.ch06

import kotlin.math.ceil
import kotlin.math.sqrt


fun main() {

    // 6-1 bad case
    (100 until 200).map { it * 2 }
        .filter { it % 3 == 0 }
        .first()

    // 6-2
    (100 until 200).map { it * 2 }
        .first { it % 3 == 0 }


    // 6-3
    (100 until 2_000_000).asSequence()
        .map {
            println("doubling $it")
            it * 2
        }
        .filter {
            println("filtering $it")
            it % 3 == 0
        }
        .first()

    // 6-7
    println(firstNPrimes(10))
    println(primesLessThan(10))
    println(primesLessThan2(10))
}

// 6-4
val numSequence1 = sequenceOf(3, 1, 4, 1, 5, 9)
val numSequence2 = listOf(3, 1, 4, 1, 5, 9).asSequence()

// 6-5
fun Int.isPrime() =
    this == 2 || (2..ceil(sqrt(this.toDouble())).toInt())
        .none { divisor -> this % divisor == 0 }

// 6-7
fun nextPrime(num: Int) =
    generateSequence(num + 1) { it + 1 }
        .first(Int::isPrime)

fun firstNPrimes(count: Int) =
    generateSequence(2, ::nextPrime)
        .take(count)
        .toList()

// 6-8
fun primesLessThan(max: Int): List<Int> =
    generateSequence(2) { n -> if (n < max) nextPrime(n) else null }
        .toList()
        .dropLast(1)

// 6-9
fun primesLessThan2(max: Int): List<Int> =
    generateSequence(2, ::nextPrime)
        .takeWhile { it < max }
        .toList()

// 6-11
fun fibonacciSequence() = sequence {
    var terms = Pair(0, 1)

    while (true) {
        yield(terms.first)
        terms = terms.second to terms.first + terms.second
    }
}