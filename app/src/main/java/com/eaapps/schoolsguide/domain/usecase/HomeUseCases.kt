package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.entity.SliderResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.domain.repository.HomeRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject


class LoadSchoolTypeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): Resource<List<TypeResponse.TypeData>> = homeRepository.getTypeSchool()
}

class LoadSliderUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun execute(): Resource<List<SliderResponse.SliderData>> = homeRepository.getSlider()
}

class LoadRecommendedUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        homeRepository.getRecommended()

}

class LoadFeatureUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    suspend fun execute(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        homeRepository.getFeature()
}