package com.demo.cookbook.ch03

import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.closeTo
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Ch03KtTest {

    // 3-5
    @Test
    fun `check equivalence`() {
        val p1 = Product("baseball", 10.0)
        val p2 = Product("baseball", 10.0, false)

        assertEquals(p1, p2)
        assertEquals(p1.hashCode(), p2.hashCode())
    }

    @Test
    fun `create set to check equals and hashcode`() {
        val p1 = Product("baseball", 10.0)
        val p2 = Product(price = 10.0, onSale = false, name = "baseball")

        val products = setOf(p1, p2)
        assertEquals(1, products.size)
    }

    // 3-6
    @Test
    fun `change price using copy`() {
        val p1 = Product("baseball", 10.0)
        val p2 = p1.copy(price = 12.0)

        assertAll(
            { assertEquals("baseball", p2.name) },
            { MatcherAssert.assertThat(p2.price, Matchers.`is`(Matchers.closeTo(12.0, 0.01))) },
            { assertFalse(p2.onSale) },
        )
    }

    // 3-8
    @Test
    fun `data copy function is shallow`() {
        val item1 = OrderItem(Product("baseball", 10.0), 5)
        val item2 = item1.copy()

        assertAll(
            { assertTrue(item1 == item2) },
            { assertFalse(item1 === item2) },
            { assertTrue(item1.product == item2.product) },
            { assertTrue(item1.product === item2.product) }
        )
    }

    // 3-9
    @Test
    fun `destructure using component functions`() {
        val p = Product("baseball", 10.0)

        val (name, price, sale) = p
        assertAll(
            { assertEquals(p.name, name) },
            { assertThat(p.price, `is`(closeTo(price, 0.01))) },
            { assertFalse(sale) },
        )
    }

    // 3-11
    @Test
    fun `load messages`() {
        val customer = Customer("Fred").apply { message }
        assertEquals(3, customer.message.size)
    }

    @Test
    fun `unary point`() {
        val point = Point(10, 20)

        assertEquals(-point, Point(-10, -20))
    }
}