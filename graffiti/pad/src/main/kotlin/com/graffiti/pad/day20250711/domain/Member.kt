package com.graffiti.pad.day20250711.domain

class Member {

    var id: Long? = null
        set(value) {
            if (value == null || value <= 0) {
                throw IllegalArgumentException("ID must be a positive number")
            }
            field = value
        }
        get() {
            return field ?: throw IllegalStateException("ID is not set")
        }

    var email: String? = null
        set(value) {
            if (value == null || value.isBlank()) {
                throw IllegalArgumentException("Email cannot be blank")
            }
            if (!value.contains("@")) {
                throw IllegalArgumentException("Email must contain '@'")
            }
            field = value
        }
        get() {
            return field ?: throw IllegalStateException("Email is not set")
        }
}