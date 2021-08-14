package com.eaapps.schoolsguide.domain.repository

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadAllTypedSchool(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>
}