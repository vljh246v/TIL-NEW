package com.graffiti.pad.day20250711.domain

class Study {

    private var owner: Member? = null

    fun setOwner(member: Member) {
        owner = member
    }
}