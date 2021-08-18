package com.eaapps.schoolsguide.domain.usecase

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.repository.FavoriteRepository
import com.eaapps.schoolsguide.domain.repository.ProfileRepository
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {

    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        favoriteRepository.toggleFavorite(schoolId)
}

class LoadFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        favoriteRepository.loadFavorite()
}

class ToggleFollowUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        profileRepository.toggleFollow(schoolId)
}

class ToggleRecommendedUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        profileRepository.toggleRecommendedIt(schoolId)
}



