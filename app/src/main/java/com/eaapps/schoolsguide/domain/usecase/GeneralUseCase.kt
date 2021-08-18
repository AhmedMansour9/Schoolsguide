package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.repository.CityRepository
import com.eaapps.schoolsguide.domain.repository.GeneralRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject


class GetCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend fun execute(): Resource<List<String>> = cityRepository.getCities()
}


class ProgramSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<ProgramsResponse.Programs>> =
        generalRepository.getSchoolPrograms()
}


class GradesSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<GradesResponse.Grades>> =
        generalRepository.getSchoolGrades()
}


class CitiesUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<CityResponse.City>> =
        generalRepository.getCities()
}

class SchoolDetailsUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(schoolId:Int): Resource<SchoolResponse.SchoolData.DataSchool> =
        generalRepository.getSchoolDetails(schoolId)
}
