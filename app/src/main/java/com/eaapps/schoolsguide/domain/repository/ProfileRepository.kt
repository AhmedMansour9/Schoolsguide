package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.utils.Resource

interface ProfileRepository {

    suspend fun addSchool(addSchoolEntity: AddSchoolEntity): Resource<ResponseEntity>

    suspend fun updatePassword(changePasswordEntity: ChangePasswordEntity): Resource<ResponseEntity>

    suspend fun updateProfileFather(changeFatherProfileEntity: ChangeFatherProfileEntity): Resource<ResponseEntity>

    suspend fun toggleRecommendedIt(schoolId: Int): Resource<ResponseEntity>

    suspend fun toggleFollow(schoolId: Int): Resource<ResponseEntity>

    suspend fun putReview(reviewRequestEntity: ReviewRequestEntity): Resource<ResponseEntity>

}