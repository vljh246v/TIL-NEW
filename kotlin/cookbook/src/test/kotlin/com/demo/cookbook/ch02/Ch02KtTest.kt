package com.demo.cookbook.ch02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.math.pow

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

    // 2-28
    @Test
    fun `raise an Int to a power`() {
        assertThat(256).isEqualTo(2.toDouble().pow(8).toInt())
    }

    // 2-30
    @Test
    fun `raise to power`() {
        assertAll(
            { assertThat(1).isEqualTo(2 `**` 0) },
            { assertThat(2).isEqualTo(2 `**` 1) },
            { assertThat(4).isEqualTo(2 `**` 2) },
            { assertThat(8).isEqualTo(2 `**` 3) },

            { assertThat(1L).isEqualTo(2L `**` 0) },
            { assertThat(2L).isEqualTo(2L `**` 1) },
            { assertThat(4L).isEqualTo(2L `**` 2) },
            { assertThat(8L).isEqualTo(2L `**` 3) },

            { assertThat(1F).isEqualTo(2F `**` 0) },
            { assertThat(2F).isEqualTo(2F `**` 1) },
            { assertThat(4F).isEqualTo(2F `**` 2) },
            { assertThat(8F).isEqualTo(2F `**` 3) }
        )
    }

    // 2-31
    @Test
    fun `double and halving`() {
        assertAll(
            "left shifts doubling form 1", // 0000_0001
            { assertThat(2).isEqualTo(1 shl 1) }, // 0000_0010
            { assertThat(4).isEqualTo(1 shl 2) }, // 0000_0100
            { assertThat(8).isEqualTo(1 shl 3) }, // 0000_1000
            { assertThat(16).isEqualTo(1 shl 4) }, // 0001_0000
            { assertThat(32).isEqualTo(1 shl 5) }, // 0010_0000
            { assertThat(64).isEqualTo(1 shl 6) }, // 0100_0000
            { assertThat(128).isEqualTo(1 shl 7) }, // 1000_0000
        )

        assertAll(
            "right shifts halving form 235", // 1110_1011
            { assertThat(117).isEqualTo(235 shr 1) }, // 0111_0101
            { assertThat(58).isEqualTo(235 shr 2) }, // 0011_1010
            { assertThat(29).isEqualTo(235 shr 3) }, // 0001_1101
            { assertThat(14).isEqualTo(235 shr 4) }, // 0000_1110
            { assertThat(7).isEqualTo(235 shr 5) }, // 0000_0111
            { assertThat(3).isEqualTo(235 shr 6) }, // 0000_0011
            { assertThat(1).isEqualTo(235 shr 7) }, // 0000_0001
        )
    }

    // 2-44
    @Test
    fun `중간값 찾기`() {
        val high = (0.99 * Int.MAX_VALUE).toInt()
        val low = (0.75 * Int.MAX_VALUE).toInt()

        val mid1 = (high + low) / 2
        val mid2 = (high + low) ushr 1

        assertThat(mid1).isNotIn(low..high)
        assertThat(mid2).isIn(low..high)
    }

    // 2-34
    @Test
    fun `4 비트 반전`() {
        // 4 == 0000_0100
        // 4.inv() == 1111_1011 ???
        assertThat(-5).isEqualTo(4.inv())
        // 2의 보수 작업
    }

    // 2-35
    @Test
    fun `and, or, xor`() {
        val n1 = 0b0000_1100 // 십진수 12
        val n2 = 0b0001_1001 // 십진수 25

        val n1AndN2 = n1 and n2
        val n1OrN2 = n1 or n2
        val n1XorN2 = n1 xor n2

        assertThat(n1AndN2).isEqualTo(0b0000_1000)
        assertThat(n1OrN2).isEqualTo(0b0001_1101)
        assertThat(n1XorN2).isEqualTo(0b0001_0101)
    }

    // 2-40
    @Test
    fun `create map using infix to function`() {
        val map = mapOf("a" to 1, "b" to 2, "c" to 2)

        assertAll(
            { assertThat(map).containsKeys("a") },
            { assertThat(map).containsKeys("b") },
            { assertThat(map).containsKeys("c") },
            { assertThat(map).containsValues(1) },
            { assertThat(map).containsValues(2) },
        )
    }

    @Test
    fun `create a Pair from constructor vs to function`() {
        val p1 = Pair("a", 1)
        val p2 = "a" to 1

        assertAll(
            { assertThat(p1.first).isEqualTo("a") },
            { assertThat(p1.second).isEqualTo(1) },
            { assertThat(p2.first).isEqualTo("a") },
            { assertThat(p2.second).isEqualTo(1) },
            { assertThat(p1).isEqualTo(p2) },
        )
    }

    // 2-42
    @Test
    fun `destructuring a Pair`() {
        val pair = "a" to 1
        val (x, y) = pair

        assertThat(x).isEqualTo("a")
        assertThat(y).isEqualTo(1)
    }
}