package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.utils.Resource

interface GeneralRepository {

    suspend fun getSchoolPrograms():Resource<List<ProgramsResponse.Programs>>

    suspend fun getSchoolGrades():Resource<List<GradesResponse.Grades>>

    suspend fun getCities(): Resource<List<CityResponse.City>>


}