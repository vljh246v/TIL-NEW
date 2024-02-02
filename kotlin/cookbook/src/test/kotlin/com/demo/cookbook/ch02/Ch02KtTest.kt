package com.demo.cookbook.ch02

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class Ch02KtTest {

    // 2-13
    @Test
    fun `check all overloads`() {
        assertAll(
            "Overloads called from Kotlin",
            { println(addProduct("Name", 5.0, "Desc")) },
            { println(addProduct("Name", 5.0)) },
            { println(addProduct("Name")) },
        )
    }

    // 2-19
    @Test
    internal fun `check overloaded Product contructor`() {
        assertAll(
            "Overloads called from Kotlin",
            { println(Product("Name", 5.0, "Desc")) },
            { println(Product("Name", 5.0)) },
            { println(Product("Name")) }
        )
    }
}