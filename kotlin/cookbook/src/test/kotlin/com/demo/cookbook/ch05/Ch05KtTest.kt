package com.demo.cookbook.ch05

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

// 5-5
class Ch05KtTest {
    @Test
    fun `valid indices`() {
        val strings = arrayOf("this", "is", "an", "array", "of", "strings")
        val indices = strings.indices
        assertThat(indices, contains(0, 1, 2, 3, 4, 5))
    }

    // 5-6
    @Test
    fun `withIndex returns IndexValues`() {
        val strings = arrayOf("this", "is", "an", "array", "of", "strings")
        for ((index, value) in strings.withIndex()) {
            println("Index $index map to $value")
            assertTrue(index in 0..5)
        }
    }
}