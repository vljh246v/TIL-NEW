package com.graffiti.pad.day20250711.member

import com.graffiti.pad.day20250711.domain.Member

interface MemberService {

    fun findById(id: Long): Member?

    fun validate(memberId: Long)
}