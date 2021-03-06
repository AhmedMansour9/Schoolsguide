package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.di.InterceptorAuthorization
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileFatherStoredUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    fun execute(): Flow<Resource<AuthResponse.AuthData>> =
        dataStoreRepository.loadFatherFromStoreData()
}

class LoadTokenUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    fun execute(): String? =
        dataStoreRepository.loadSessionToken()
}

class LoadLanguageUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    fun execute(): String? =
        dataStoreRepository.loadLanguage()
}

class SaveLanguageUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val interceptorAuthorization: InterceptorAuthorization
) {
    fun execute(lang: String) {
        interceptorAuthorization.language = lang
        dataStoreRepository.saveLanguage(lang)
    }
}