package com.graffiti.pad.day20250711.study

import kotlin.test.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
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
}