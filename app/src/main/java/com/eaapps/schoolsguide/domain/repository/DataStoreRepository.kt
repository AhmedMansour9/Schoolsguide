package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveFatherData(authDataResponse: AuthResponse.AuthData)

    fun loadFatherFromStoreData(): Flow<Resource<AuthResponse.AuthData>>

    fun saveTokenSession(accessToken: String)

    fun loadSessionToken(): String?

    fun saveLanguage(lang: String)

    fun loadLanguage(): String?
}