package com.demo.cookbook.ch04

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}