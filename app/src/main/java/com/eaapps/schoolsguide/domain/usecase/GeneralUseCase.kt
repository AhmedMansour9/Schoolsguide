package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.City
import com.eaapps.schoolsguide.domain.repository.CityRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject


class GetCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend fun execute(): Resource<List<String>> = cityRepository.getCities()
}