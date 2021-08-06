package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.City
import com.eaapps.schoolsguide.utils.Resource

interface CityRepository {
    suspend fun getCities(): Resource<List<String>>
}