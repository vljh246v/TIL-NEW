package com.graffiti.pad.day20250711.domain

class Member {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        return result
    }


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