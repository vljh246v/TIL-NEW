package com.demo.cookbook.ch06

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ch06KtTest {

    // 6-13
    @Test
    fun `first 10 Fibonacci numbers from sequence`() {
        val fibs = fibonacciSequence()
            .take(10)
            .toList()

        assertEquals(listOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), fibs)
    }


    // 6-14
    @Test
    fun `yield All sequence`() {
        val zeroSequence = sequence {
            val start = 0
            yield(start)
            yieldAll(generateSequence(8) { it * 3 })
        }

        val toList = zeroSequence
            .take(10)
            .toList()

        println(toList)
    }
}