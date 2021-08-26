package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.ApiServices
import com.eaapps.schoolsguide.domain.repository.ProfileRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.resourceError
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val apiServices: ApiServices) : ProfileRepository {

    override suspend fun addSchool(
        addSchoolEntity: AddSchoolEntity,
    ): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.addSchoolAsync(addSchoolEntity).await()
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

    override suspend fun updatePassword(changePasswordEntity: ChangePasswordEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.changePasswordAsync(changePasswordEntity).await()
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

    override suspend fun updateProfileFather(
        changeFatherProfileEntity: ChangeFatherProfileEntity,
    ): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result =
                    apiServices.changeFatherProfileAsync(
                        "put".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.full_name.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.email.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.phone.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        "${changeFatherProfileEntity.city_id}".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.gender?.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.image
                    ).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val errorBody = result.errorBody()?.let {
                        val type = object : TypeToken<ResponseEntity>() {}.type
                        Gson().fromJson(it.charStream().readText(), type) as ResponseEntity?
                    }

                    if (errorBody != null) {
                        Resource.Error(result.code(), errorBody.message)
                    } else
                        resourceError(result.code())

                }
            }, "Exception occurred!")
        }

    override suspend fun toggleRecommendedIt(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.toggleRecommendedAsync(schoolId).await()
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

    override suspend fun toggleFollow(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.toggleFollowAsync(schoolId).await()
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

    override suspend fun putReview(reviewRequestEntity: ReviewRequestEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.putReviewAsync(reviewRequestEntity).await()
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