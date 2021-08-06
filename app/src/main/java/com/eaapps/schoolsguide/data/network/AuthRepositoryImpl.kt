package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val dataStoreRepository: DataStoreRepository
) :
    AuthRepository {

    override suspend fun login(loginEntity: LoginEntity): Resource<DataAuth> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loginAsync(loginEntity).await()
                if (result.isSuccessful) {
                    dataStoreRepository.saveFatherData(result.body()?.data!!)
                    Resource.Success(result.body()?.data)
                } else {
                    val type = object : TypeToken<ResponseError>() {}.type
                    val responseError: ResponseError? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseError?.message ?: result.message())
                }
            }, "Exception occurred!")
        }


    override suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        city: Int,
        district: String,
        password: String,
        confirmPassword: String
    ): Resource<DataAuth> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.registerAsync(
                    RegisterEntity(
                        fullName,
                        email,
                        city.toString(),
                        password,
                        confirmPassword,
                        phone,
                        district
                    )
                ).await()
                if (result.isSuccessful)
                    Resource.Success(result.body()?.data)
                else {
                    val type = object : TypeToken<ResponseError>() {}.type
                    val responseError: ResponseError? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseError?.message ?: result.message())
                }
            }, "Exception occurred!")
        }


    override suspend fun loginBySocial(
        provider: String,
        social_id: String,
        email: String,
        fullName: String
    ): Resource<DataAuth> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loginBySocialAsync(
                    SocialEntity(
                        provider,
                        social_id,
                        email,
                        fullName
                    )
                ).await()
                if (result.isSuccessful)
                    Resource.Success(result.body()?.data)
                else {
                    val type = object : TypeToken<ResponseError>() {}.type
                    val responseError: ResponseError? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseError?.message ?: result.message())
                }
            }, "Exception occurred!")
        }
}