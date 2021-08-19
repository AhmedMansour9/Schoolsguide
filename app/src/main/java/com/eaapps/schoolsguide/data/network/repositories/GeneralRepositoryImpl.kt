package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.ApiServices
import com.eaapps.schoolsguide.domain.repository.GeneralRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeneralRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    GeneralRepository {

    override suspend fun getSchoolPrograms(): Resource<List<ProgramsResponse.Programs>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getSchoolProgramsAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data)
                } else {
                    Resource.Error(result.code(), result.message())
                }
            }, "")
        }

    override suspend fun getSchoolGrades(): Resource<List<GradesResponse.Grades>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getSchoolGradesAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data)
                } else {
                    Resource.Error(result.code(), result.message())
                }
            }, "")
        }

    override suspend fun getCities(): Resource<List<CityResponse.City>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getCitiesAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data)
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }

            }, "Exception occurred!")
        }

    override suspend fun getSchoolDetails(schoolId: Int): Resource<SchoolResponse.SchoolData.DataSchool> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.schoolDetailsAsync(schoolId).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data)
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }

            }, "Exception occurred!")
        }

    override suspend fun addInquiry(addInquiryRequestEntity: InquiryRequestEntity) =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.addInquiryAsync(addInquiryRequestEntity).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream().readText(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun joinDiscount(discountRequestEntity: DiscountRequestEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.joinDiscountAsync(discountRequestEntity).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream().readText(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun bookNow(body: HashMap<String, Any>): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.bookSchoolAsync(body).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream().readText(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

}