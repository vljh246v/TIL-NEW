package com.demo.cookbook.ch05

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

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

    // 5-10
    @Test
    internal fun `instantiating a linked list`() {
        val list = LinkedList<Int>()
        list.add(3)
        list.add(1)
        list.addLast(999)
        list[2] = 4
        list.addAll(listOf(1, 5, 9, 2, 6, 5))
        assertThat(list, contains(3, 1, 4, 1, 5, 9, 2, 6, 5))
    }
}