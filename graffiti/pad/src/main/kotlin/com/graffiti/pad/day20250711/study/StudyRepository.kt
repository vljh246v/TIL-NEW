package com.graffiti.pad.day20250711.study

import com.graffiti.pad.day20250711.domain.Study

interface StudyRepository {

    fun save(study: Study): Study
}