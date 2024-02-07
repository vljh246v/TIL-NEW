package com.demo.cookbook.ch05

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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

    // 5-16
    private val overthruster = Product("Oscillation Overthruster", 1_000_000.0)
    private val fluxcapacitro = Product("Flux Capacitor", 299_999.95, true)
    private val tpsReportCoverSheet = Product("TPS Report Cover Sheet", 0.25)

    @Test
    fun productsOnSale() {
        val products = listOf(overthruster, fluxcapacitro, tpsReportCoverSheet)
        assertAll(
            "On Sale products",
            { assertEquals("Flux Capacitor", onSaleProducts_ifEmptyCollection(products)) },
            { assertEquals("Flux Capacitor", onSaleProducts_ifEmptyString(products)) },
        )
    }

    @Test
    fun productsNotOnSale() {
        val products = listOf(overthruster, tpsReportCoverSheet)
        assertAll(
            "No Products on sale",
            { assertEquals("none", onSaleProducts_ifEmptyCollection(products)) },
            { assertEquals("none", onSaleProducts_ifEmptyString(products)) },

            )
    }

    // 5-17
    @Test
    fun `coerceIn given a range`() {
        val range = 3..8
        assertThat(5, `is`(5.coerceIn(range)))
        assertThat(range.first, `is`(1.coerceIn(range)))
        assertThat(range.last, `is`(9.coerceIn(range)))
    }

    // 5-18
    @Test
    fun `coerceIn given min and max`() {
        val min = 2
        val max = 6
        assertThat(5, `is`(5.coerceIn(min, max)))
        assertThat(min, `is`(1.coerceIn(min, max)))
        assertThat(max, `is`(9.coerceIn(min, max)))
    }

    // 5-19
    @Test
    fun chunked() {
        val range = 0..10
        val chunked = range.chunked(3)
        assertThat(chunked, contains(listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), listOf(9, 10)))
        assertThat(range.chunked(3) { it.sum() }, `is`(listOf(3, 12, 21, 19)))
        assertThat(range.chunked(3) { it.average() }, `is`(listOf(1.0, 4.0, 7.0, 9.5)))
    }

    // 5-21
    @Test
    fun windowed() {
        val range = 0..10
        assertThat(range.windowed(3, 3), contains(listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8)))
        assertThat(range.windowed(3, 3) { it.average() }, contains(1.0, 4.0, 7.0))
        assertThat(
            range.windowed(3, 1),
            contains(
                listOf(0, 1, 2), listOf(1, 2, 3), listOf(2, 3, 4),
                listOf(3, 4, 5), listOf(4, 5, 6), listOf(5, 6, 7),
                listOf(6, 7, 8), listOf(7, 8, 9), listOf(8, 9, 10)
            )
        )
        assertThat(
            range.windowed(3, 1) { it.average() },
            contains(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
        )
    }
}