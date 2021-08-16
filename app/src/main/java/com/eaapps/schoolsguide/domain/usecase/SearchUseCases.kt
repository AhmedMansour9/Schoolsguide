package com.eaapps.schoolsguide.domain.usecase

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.FilterRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.model.FilterModel
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


class LoadFilterSchoolsUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend fun execute(filterModel: FilterModel): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        searchRepository.loadAllSchoolsByFilter(
            FilterRequestEntity(
                filterModel.search,
                filterModel.school_type,
                filterModel.type_id,
                filterModel.grade_id,
                filterModel.from_price,
                filterModel.to_price,
                filterModel.program_id,
                filterModel.city_id,
                filterModel.review
            )
        )
}