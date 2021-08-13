package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.utils.Resource

interface AuthRepository {

    suspend fun login(loginEntity: LoginEntity): Resource<AuthResponse.AuthData>

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        city: Int,
        district: String,
        password: String,
        confirmPassword: String
    ): Resource<AuthResponse.AuthData>

    suspend fun loginBySocial(provider: String, social_id: String, email: String, fullName: String)
            : Resource<AuthResponse.AuthData>


    suspend fun getProfileFather(): Resource<AuthResponse.AuthData>

}