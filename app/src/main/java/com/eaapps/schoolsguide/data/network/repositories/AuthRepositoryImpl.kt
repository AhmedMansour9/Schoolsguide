package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.ApiServices
import com.eaapps.schoolsguide.di.InterceptorAuthorization
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val dataStoreRepository: DataStoreRepository,
    private val interceptorAuthorization: InterceptorAuthorization
) : AuthRepository {

    override suspend fun login(loginEntity: LoginEntity): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loginAsync(loginEntity)
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                dataStoreRepository.saveFatherData(result.dataResponse)
                Resource.Success(result.dataResponse)
            }, "Exception occurred!")
        }

    override suspend fun register(fullName: String, email: String, phone: String, city: Int, district: String, password: String, confirmPassword: String): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.registerAsync(
                    RegisterEntity(fullName, email, city.toString(), password, confirmPassword, phone, district))
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                Resource.Success(result.dataResponse)
            }, "Exception occurred!")
        }

    override suspend fun loginBySocial(provider: String, social_id: String, email: String, fullName: String): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loginBySocialAsync(SocialEntity(provider, social_id, email, fullName))
                val token = result.dataResponse?.access_token!!
                interceptorAuthorization.token = token
                dataStoreRepository.saveTokenSession(token)
                Resource.Success(result.dataResponse)
            }, "Exception occurred!")
        }

    override suspend fun resetPassword(resetPasswordRequestEntity: ResetPasswordRequestEntity): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.resetPasswordAsync(resetPasswordRequestEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun createPassword(email: String): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val hashCreatePassword = HashMap<String, String>().apply {
                    put("email", email)
                }
                val result = apiServices.createPasswordAsync(hashCreatePassword)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun getProfileFather(): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.loadProfileFatherAsync()
                Resource.Success(result.dataResponse)

            }, "Exception occurred!")
        }

    override suspend fun logoutFather(): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.logoutAsync()
                dataStoreRepository.saveTokenSession("")
                Resource.Success(result)
            }, "Exception occurred!")
        }
}