package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.entity.SliderResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.utils.Resource

interface HomeRepository {

    suspend fun getTypeSchool(): Resource<List<TypeResponse.TypeData>>

    suspend fun getSlider(): Resource<List<SliderResponse.SliderData>>

    suspend fun getRecommended(): Resource<List<SchoolResponse.SchoolData.DataSchool>>

    suspend fun getFeature(): Resource<List<SchoolResponse.SchoolData.DataSchool>>
}