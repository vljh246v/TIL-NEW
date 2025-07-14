package com.graffiti.pad.day20250711.domain

class Study(
    val limit: Int,
    val name: String,
) {

    var owner: Member? = null
        set(value) {
            if (value != null)
                field = value
        }
}