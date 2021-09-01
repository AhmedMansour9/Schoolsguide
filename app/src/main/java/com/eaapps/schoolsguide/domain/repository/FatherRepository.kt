package com.eaapps.schoolsguide.domain.repository

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.ReviewRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FatherRepository {

    suspend fun toggleFavorite(schoolId: Int): Resource<ResponseEntity>

    suspend fun loadFavorite(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadFollow(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun toggleRecommendedIt(schoolId: Int): Resource<ResponseEntity>

    suspend fun toggleFollow(schoolId: Int): Resource<ResponseEntity>

    suspend fun putReview(reviewRequestEntity: ReviewRequestEntity): Resource<ResponseEntity>

    suspend fun getProfileFather(): Resource<AuthResponse.AuthData>

}