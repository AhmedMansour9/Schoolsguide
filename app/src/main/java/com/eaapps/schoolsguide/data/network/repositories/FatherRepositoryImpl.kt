package com.eaapps.schoolsguide.data.network.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.ReviewRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.network.apiServices.FatherApis
import com.eaapps.schoolsguide.data.network.pagingDataSources.FavoritePagingDataSource
import com.eaapps.schoolsguide.data.network.pagingDataSources.FollowPagingDataSource
import com.eaapps.schoolsguide.domain.repository.FatherRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FatherRepositoryImpl @Inject constructor(private val fatherApis: FatherApis) :
    FatherRepository {

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    override suspend fun toggleFavorite(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = fatherApis.toggleFavoriteAsync(schoolId)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun loadFavorite(
    ): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> {


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FavoritePagingDataSource(fatherApis) }).flow
    }


    override suspend fun loadFollow(
    ): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> {


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FollowPagingDataSource(fatherApis) }).flow
    }

    override suspend fun getProfileFather(): Resource<AuthResponse.AuthData> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = fatherApis.loadProfileFatherAsync()
                Resource.Success(result.dataResponse)

            }, "Exception occurred!")
        }


    override suspend fun toggleRecommendedIt(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = fatherApis.toggleRecommendedAsync(schoolId)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun toggleFollow(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = fatherApis.toggleFollowAsync(schoolId)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun putReview(reviewRequestEntity: ReviewRequestEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = fatherApis.putReviewAsync(reviewRequestEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

}