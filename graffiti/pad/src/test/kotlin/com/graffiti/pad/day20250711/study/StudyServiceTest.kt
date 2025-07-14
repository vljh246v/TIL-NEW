package com.graffiti.pad.day20250711.study

import kotlin.test.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
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

            override fun notify(newStudy: Study) {
                TODO("Not yet implemented")
            }

            override fun notify(newStudy: Member) {
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

    @Test
    fun throwIllegalArgumentWhenCallValidationMethod() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)

        // When & Then
        doThrow(IllegalArgumentException()).`when`(memberService).validate(1L)

        assertThrows(IllegalArgumentException::class.java) {
            memberService.validate(1L)
        }
    }

    @Test
    fun testMemberServiceFindById() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)

        val member = Member()
        member.id = 1L
        member.email = "tes@test.com"

        `when`(memberService.findById(anyLong()))
            .thenReturn(member)
            .thenThrow(IllegalArgumentException())
            .thenReturn(null)

        val first = memberService.findById(1L)
        assertEquals(1L, first?.id)

        assertThrows(IllegalArgumentException::class.java) {
            memberService.findById(2L)
        }

        val second = memberService.findById(3L)
        assertNull(second)
    }

    @Test
    fun createNewStudy() {
        // Given
        val memberService = Mockito.mock(MemberService::class.java)
        val studyRepository = Mockito.mock(StudyRepository::class.java)

        val studyService = StudyService(studyRepository, memberService)

        val member = Member()
        member.id = 1L
        member.email = "test@test.com"

        val study = Study(10, "Test Study")

        `when`(memberService.findById(1L)).thenReturn(member)
        `when`(studyRepository.save(study)).thenReturn(study)

        val createStudy = studyService.createStudy(study, 1L)

        assertNotNull(createStudy.name)
        assertEquals(member, createStudy.owner)
        verify(memberService, times(1)).notify(createStudy)
//        verifyNoMoreInteractions(memberService)
        verify(memberService, times(1)).notify(member)
        verify(memberService, never()).validate(anyLong())

        val inOrder = inOrder(memberService)
        inOrder.verify(memberService).notify(createStudy)
        inOrder.verify(memberService).notify(member)
    }
}