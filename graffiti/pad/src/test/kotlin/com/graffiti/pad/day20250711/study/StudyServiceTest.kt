package com.graffiti.pad.day20250711.study

import kotlin.test.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import com.graffiti.pad.day20250711.domain.Member
import com.graffiti.pad.day20250711.domain.Study
import com.graffiti.pad.day20250711.member.MemberService

@ExtendWith(MockitoExtension::class)
class StudyServiceTest {

    @Test
    fun createStudyServiceTest() {
        // Given
        val memberService = object : MemberService {
            override fun findById(id: Long): Member? {
                return null
            }

            override fun validate(memberId: Long) {
                TODO("Not yet implemented")
            }
        }

        val studyRepository = object : StudyRepository {
            override fun save(study: Study): Study {
                return study
            }
        }

        // When
        val studyService = StudyService(studyRepository, memberService)

        // Then
        assertNotNull(studyService)
    }

    @Test
    fun createStudyServiceWithMockTest() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)
        val studyRepository = Mockito.mock(StudyRepository::class.java)

        // When
        val studyService = StudyService(studyRepository, memberService)

        // Then
        assertNotNull(studyService)
    }

    @Test
    fun createStudyServiceWithMockAnnotationTest(
        @Mock studyRepository: StudyRepository,
        @Mock memberService: MemberService
    ) {
        // When
        val studyService = StudyService(studyRepository, memberService)

        // Then
        assertNotNull(studyService)
    }

    @Test
    fun testMockitoDefaultReturnValue() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)
        val studyRepository = Mockito.mock(StudyRepository::class.java)

        // When
        val member = memberService.findById(1L)
        memberService.validate(2L)

        // Then
        assertNull(member)
    }

    @Test
    fun createMember() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)

        val member = Member()
        member.id = 1L
        member.email = "tes@test.com"

        // When
        `when`(memberService.findById(1L)).thenReturn(member)

        // Then
        val findById = memberService.findById(1L)
        assertEquals(1L, findById?.id)
        assertEquals("tes@test.com", findById?.email)
    }
}