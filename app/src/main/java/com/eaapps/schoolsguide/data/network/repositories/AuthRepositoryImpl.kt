package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.ApiServices
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
) : AuthRepository {

    override suspend fun login(loginEntity: LoginEntity): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loginAsync(loginEntity).await()
                if (result.isSuccessful) {
                    dataStoreRepository.saveTokenSession(result.body()?.dataResponse?.access_token!!)
                    dataStoreRepository.saveFatherData(result.body()?.dataResponse!!)
                    Resource.Success(result.body()?.dataResponse)
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
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
    ): Resource<AuthResponse.AuthData> =
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
                if (result.isSuccessful) {
                    dataStoreRepository.saveTokenSession(result.body()?.dataResponse?.access_token!!)
                    Resource.Success(result.body()?.dataResponse)
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }


    override suspend fun loginBySocial(
        provider: String,
        social_id: String,
        email: String,
        fullName: String
    ): Resource<AuthResponse.AuthData> =
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
                if (result.isSuccessful) {
                    dataStoreRepository.saveTokenSession(result.body()?.dataResponse?.access_token!!)
                    Resource.Success(result.body()?.dataResponse)
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun resetPassword(resetPasswordRequestEntity: ResetPasswordRequestEntity): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.resetPasswordAsync(resetPasswordRequestEntity).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun createPassword(email: String): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.createPasswordAsync(HashMap<String, String>().apply {
                    put("email", email)
                }).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun getProfileFather(): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                if (dataStoreRepository.loadSessionToken()?.isNotBlank()!!) {
                    val result = apiServices.loadProfileFatherAsync().await()
                    if (result.isSuccessful) {
                        Resource.Success(result.body()?.dataResponse)
                    } else {
//                        val type = object : TypeToken<ResponseEntity>() {}.type
//                        val responseFailure: ResponseEntity? =
//                            Gson().fromJson(result.errorBody()!!.charStream(), type)
                        Resource.Error(404,"Father Not Login")

                     //   Resource.Error(result.code(), responseFailure?.message ?: result.message())
                    }
                }else{
                    Resource.Error(404,"Father Not Login")

                }
            }, "Exception occurred!")
        }

    override suspend fun logoutFather(): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.logoutAsync().await()
                if (result.isSuccessful) {
                    dataStoreRepository.saveTokenSession("")
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