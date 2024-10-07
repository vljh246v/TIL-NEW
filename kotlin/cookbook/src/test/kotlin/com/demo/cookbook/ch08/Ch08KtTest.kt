package com.demo.cookbook.ch08

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Ch08KtTest {

    // 8-3
    @Test
    fun `Dialing delegates to internal phone`() {
        val smartPhone = SmartPhone()
        assertEquals("Dialing 555-1234...", smartPhone.dial("555-1234"))
    }

    @Test
    fun `Taking picture delegates to internal camera`() {
        val smartPhone = SmartPhone()
        assertEquals("Taking picture...", smartPhone.takePicture())

    }
}