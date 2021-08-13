package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.AddSchoolEntity
import com.eaapps.schoolsguide.data.entity.ChangeFatherProfileEntity
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.repository.ProfileRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.resourceError
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    ProfileRepository {

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
                    apiServices.changeFatherProfileAsync(changeFatherProfileEntity)
                        .await()
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

}