package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.entity.SliderResponse
import com.eaapps.schoolsguide.domain.repository.HomeRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    HomeRepository {
    override suspend fun getSlider(): Resource<List<SliderResponse.SliderData>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getSliderAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data)
                } else {
                    Resource.Error(result.code(), result.message())
                }
            }, "")
        }

    override suspend fun getRecommended(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getRecommendedSchoolAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data?.data)
                } else {
                    Resource.Error(result.code(), result.message())
                }
            }, "")
        }

    override suspend fun getFeature(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.getFeaturedSchoolAsync().await()
                if (result.isSuccessful) {
                    Resource.Success(result.body()?.data?.data)
                } else {
                    Resource.Error(result.code(), result.message())
                }
            }, "")
        }
}