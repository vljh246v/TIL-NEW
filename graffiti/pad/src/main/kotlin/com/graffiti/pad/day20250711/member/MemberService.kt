package com.graffiti.pad.day20250711.member

import com.graffiti.pad.day20250711.domain.Member
import com.graffiti.pad.day20250711.domain.Study

interface MemberService {

    fun findById(id: Long): Member?

    fun validate(memberId: Long)
    fun notify(newStudy: Study)
}