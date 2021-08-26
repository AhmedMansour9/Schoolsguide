package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.utils.Resource
import java.io.File

interface GeneralRepository {

    suspend fun getSchoolPrograms(): Resource<List<ProgramsResponse.Programs>>

    suspend fun getSchoolGrades(): Resource<List<GradesResponse.Grades>>

    suspend fun getCities(): Resource<List<CityResponse.City>>

    suspend fun getSchoolDetails(schoolId: Int): Resource<SchoolResponse.SchoolData.DataSchool>

    suspend fun addInquiry(addInquiryRequestEntity: InquiryRequestEntity): Resource<ResponseEntity>

    suspend fun joinDiscount(discountRequestEntity: DiscountRequestEntity): Resource<ResponseEntity>

    suspend fun bookNow(body: HashMap<String, Any>): Resource<ResponseEntity>

    suspend fun uploadCv(jobId:Int,schoolId: Int,file: File): Resource<ResponseEntity>

}