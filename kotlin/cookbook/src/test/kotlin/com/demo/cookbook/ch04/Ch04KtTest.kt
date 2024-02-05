package com.demo.cookbook.ch04

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class Ch04KtTest {

    // 4-2
    @Test
    fun `sum using fold`() {
        val numbers = intArrayOf(3, 1, 4, 1, 5, 9)
        assertEquals(numbers.sum(), sum(*numbers))
    }


    // 4-3
    @Test
    fun `sum using fold with trace`() {
        val numbers = intArrayOf(3, 1, 4, 1, 5, 9)
        assertEquals(numbers.sum(), sumWithTrace(*numbers))
    }

    // 4-4
    @Test
    fun `factorial using fold`() {
        val factorialFold = factorialFold(3)
        assertEquals(factorialFold, BigInteger.valueOf(6))
    }

    @Test
    fun `fibonacci using fold`() {
        val fibonacciFold = fibonacciFold(7)
        assertEquals(fibonacciFold, 13)
    }
}