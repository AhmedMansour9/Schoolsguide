package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    AuthRepository {
    override suspend fun login(loginEntity: LoginEntity): Resource<DataAuth> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.login(loginEntity).await()
                val value = result.body()
                if (result.isSuccessful)
                    Resource.Success(result.body()?.data)
                else {
                    Resource.Error(result.code(), result.body()?.message ?: result.message())
                }
            }, "Exception occurred!")
        }


    override suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        city: String,
        district: String,
        password: String
    ): Resource<DataAuth> {
        TODO("Not yet implemented")
    }

    override suspend fun loginBySocial(
        provider: String,
        social_id: String,
        email: String,
        fullName: String
    ): Resource<DataAuth> {
        TODO("Not yet implemented")
    }
}