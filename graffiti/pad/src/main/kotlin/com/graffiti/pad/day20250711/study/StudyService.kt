package com.graffiti.pad.day20250711.study

import com.graffiti.pad.day20250711.domain.Study
import com.graffiti.pad.day20250711.member.MemberService

class StudyService(
    private val studyRepository: StudyRepository,
    private val memberService: MemberService
) {

    fun createStudy(study: Study, memberId: Long): Study {
        val member = memberService.findById(memberId)
            ?: throw IllegalArgumentException("Member does not exist with id: $memberId")

        study.owner = member
        val newStudy = studyRepository.save(study)
        memberService.notify(newStudy)
        memberService.notify(member)
        return newStudy
    }
}