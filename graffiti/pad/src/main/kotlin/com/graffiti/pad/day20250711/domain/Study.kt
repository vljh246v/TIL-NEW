package com.graffiti.pad.day20250711.domain

class Study(
    val limit: Int,
    val name: String,
) {

    private var owner: Member? = null

    fun setOwner(member: Member) {
        owner = member
    }
}