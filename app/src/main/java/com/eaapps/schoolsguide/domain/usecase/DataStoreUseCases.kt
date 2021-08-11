package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileFatherStoredUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    fun execute(): Flow<Resource<AuthResponse.AuthData>> =
        dataStoreRepository.loadFatherFromStoreData()

}