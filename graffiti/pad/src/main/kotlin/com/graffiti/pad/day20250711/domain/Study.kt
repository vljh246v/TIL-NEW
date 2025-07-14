package com.graffiti.pad.day20250711.domain

import java.time.LocalDateTime

class Study(
    val limit: Int,
    val name: String,
) {
    var owner: Member? = null
        set(value) {
            if (value != null)
                field = value
        }

    var openedDateTime: LocalDateTime = LocalDateTime.now()

    var status: StudyStatus = StudyStatus.DRAFT

    fun open() {
        openedDateTime = LocalDateTime.now()
        status = StudyStatus.OPENED

    }
}