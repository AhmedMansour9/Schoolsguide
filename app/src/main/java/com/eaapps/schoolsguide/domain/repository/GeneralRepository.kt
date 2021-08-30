package com.eaapps.schoolsguide.domain.repository

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface GeneralRepository {

    suspend fun getSchoolPrograms(): Resource<List<ProgramsResponse.Programs>>

    suspend fun getSchoolGrades(): Resource<List<GradesResponse.Grades>>

    suspend fun getCities(): Resource<List<CityResponse.City>>

    suspend fun getSchoolDetails(schoolId: Int): Resource<SchoolResponse.SchoolData.DataSchool>

    suspend fun addInquiry(addInquiryRequestEntity: InquiryRequestEntity): Resource<ResponseEntity>

    suspend fun joinDiscount(discountRequestEntity: DiscountRequestEntity): Resource<ResponseEntity>

    suspend fun bookNow(body: HashMap<String, Any>): Resource<ResponseEntity>

    suspend fun uploadCv(jobId: Int, schoolId: Int, file: File): Resource<ResponseEntity>

    suspend fun filterSchools(filterRequestEntity: FilterRequestEntity): Resource<List<SchoolResponse.SchoolData.DataSchool>>

    suspend fun getTypeSchool(): Resource<List<TypeResponse.TypeData>>

    suspend fun getSlider(): Resource<List<SliderResponse.SliderData>>

    suspend fun getRecommended(): Resource<List<SchoolResponse.SchoolData.DataSchool>>

    suspend fun getFeature(): Resource<List<SchoolResponse.SchoolData.DataSchool>>

    suspend fun addSchool(addSchoolEntity: AddSchoolEntity): Resource<ResponseEntity>

    suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadAllTypedSchool(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

    suspend fun loadAllSchoolsByFilter(filterRequestEntity: FilterRequestEntity): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>>

}