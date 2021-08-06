package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.utils.Resource

interface AuthRepository {

    suspend fun login(loginEntity: LoginEntity): Resource<DataAuth>

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        city: Int,
        district: String,
        password: String,
        confirmPassword: String
    ): Resource<DataAuth>

    suspend fun loginBySocial(provider: String, social_id: String, email: String, fullName: String)
            : Resource<DataAuth>
}