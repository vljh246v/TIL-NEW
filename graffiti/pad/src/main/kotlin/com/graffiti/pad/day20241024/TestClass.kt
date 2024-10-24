package com.graffiti.pad.day20241024

import java.math.BigInteger
import kotlin.random.Random

class TestClass

val fibonacci: Sequence<BigInteger> =
    sequence {
        var first = 0.toBigInteger()
        var second = 1.toBigInteger()
        while (true) {
            yield(first)
            val temp = first
            first += second
            second = temp
        }
    }

fun randomNumbers(seed: Long = System.currentTimeMillis()): Sequence<Int> =
    sequence {
        val random = Random(seed)
        while (true) {
            yield(random.nextInt())
        }
    }

fun randomUniqueStrings(
    length: Int,
    seed: Long = System.currentTimeMillis(),
): Sequence<String> =
    sequence {
        val random = Random(seed)
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        while (true) {
            val randomString =
                (1..length)
                    .map { random.nextInt(charPool.size) }
                    .joinToString("") { charPool[it].toString() }
            yield(randomString)
        }
    }.distinct() // 고유한 문자열만 유지

fun main() {
//    print(fibonacci.take(10).toList())
//    println(randomNumbers().take(10).toList())
    println(randomUniqueStrings(3).distinct().take(10).toList())
}
