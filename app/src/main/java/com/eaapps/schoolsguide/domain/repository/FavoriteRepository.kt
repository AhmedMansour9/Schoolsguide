package com.eaapps.schoolsguide.domain.repository

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun toggleFavorite(schoolId: Int): Resource<ResponseEntity>

    suspend fun loadFavorite(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

}