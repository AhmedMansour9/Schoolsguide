package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.apiServices.AuthenticationApis
import com.eaapps.schoolsguide.di.InterceptorAuthorization
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authenticationApis: AuthenticationApis,
    private val dataStoreRepository: DataStoreRepository,
    private val interceptorAuthorization: InterceptorAuthorization
) : AuthRepository {

    override suspend fun login(loginEntity: LoginEntity): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = authenticationApis.loginAsync(loginEntity)
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                dataStoreRepository.saveFatherData(result.dataResponse)
                Resource.Success(result.dataResponse)
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
                val result = authenticationApis.registerAsync(
                    RegisterEntity(
                        fullName,
                        email,
                        city.toString(),
                        password,
                        confirmPassword,
                        phone,
                        district
                    )
                )
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                Resource.Success(result.dataResponse)
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
                val result = authenticationApis.loginBySocialAsync(
                    SocialEntity(
                        provider,
                        social_id,
                        email,
                        fullName
                    )
                )
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                Resource.Success(result.dataResponse)
            }, "Exception occurred!")
        }

    override suspend fun logoutFather(): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = authenticationApis.logoutAsync()
                dataStoreRepository.saveTokenSession("")
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun updateProfileFather(changeFatherProfileEntity: ChangeFatherProfileEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result =
                    authenticationApis.changeFatherProfileAsync(
                        "put".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.full_name.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.email.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.phone.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        "${changeFatherProfileEntity.city_id}".toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.gender?.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        changeFatherProfileEntity.image
                    )
                Resource.Success(result)
            }, "Exception occurred!")
        }
}