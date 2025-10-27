package com.demo.testing.target

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TargetClassTest {
    /*
    * 모든 인수가 null 일때?
    * open, close 가 빈 문자열일때?
    * str 이 빈 문자열일때?
    * str 에 open, close가 미포함 일때?
    * str에 open은 있고 close가 없을때
    * str에 close는 있고 open이 없을때
    * str에 open, close가 여러개 있을때
     */

    lateinit var targetClass: TargetClass

    @BeforeAll
    fun initAll() {
        targetClass = TargetClass()
    }


    @Test
    fun strIsNullOrEmpty() {

        assertThat(targetClass.substringBetween(null, "a", "b"))
            .isEqualTo(null)
        assertThat(targetClass.substringBetween("", "a", "b"))
            .isEqualTo(arrayOf<String>())
    }

    @Test
    fun openIsNullOrEmpty() {
        assertThat(targetClass.substringBetween("abc", null, "b"))
            .isEqualTo(null)
        assertThat(targetClass.substringBetween("abc", "", "b"))
            .isEqualTo(null)
    }

    @Test
    fun closeIsNullOrEmpty() {
        assertThat(targetClass.substringBetween("abc", "a", null))
            .isEqualTo(null)
        assertThat(targetClass.substringBetween("abc", "a", ""))
            .isEqualTo(null)
    }

    @Test
    fun strOfLength1() {
        assertThat(targetClass.substringBetween("a", "a", "b")).isEqualTo(null)
        assertThat(targetClass.substringBetween("a", "b", "a")).isEqualTo(null)
        assertThat(targetClass.substringBetween("a", "b", "b")).isEqualTo(null)
        assertThat(targetClass.substringBetween("a", "a", "a")).isEqualTo(null)
    }

    @Test
    fun openAndCloseOfLength1() {
        assertThat(targetClass.substringBetween("abc", "x", "y")).isEqualTo(null)
        assertThat(targetClass.substringBetween("abc", "a", "y")).isEqualTo(null)
        assertThat(targetClass.substringBetween("abc", "x", "c")).isEqualTo(null)
        assertThat(targetClass.substringBetween("abc", "a", "c")).isEqualTo(arrayOf("b"))
        assertThat(targetClass.substringBetween("abcabc", "a", "c")).isEqualTo(arrayOf("b", "b"))
    }
}