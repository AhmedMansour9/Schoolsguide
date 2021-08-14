package com.eaapps.schoolsguide.domain.usecase

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadAllRecommendedUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        searchRepository.loadAllRecommended()
}


class LoadAllFeaturedUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        searchRepository.loadAllFeatured()
}

class LoadTypedSchoolUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend fun execute(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        searchRepository.loadAllTypedSchool(typeId)
}