package com.eaapps.schoolsguide.data.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.network.dataSources.FavoritePagingDataSource
import com.eaapps.schoolsguide.domain.repository.FavoriteRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    FavoriteRepository {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    override suspend fun toggleFavorite(schoolId: Int): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = apiServices.toggleFavoriteAsync(schoolId).await()
                if (result.isSuccessful) {
                    Resource.Success(result.body())
                } else {
                    val type = object : TypeToken<ResponseEntity>() {}.type
                    val responseFailure: ResponseEntity? =
                        Gson().fromJson(result.errorBody()!!.charStream().readText(), type)
                    Resource.Error(result.code(), responseFailure?.message ?: result.message())
                }
            }, "Exception occurred!")
        }

    override suspend fun loadFavorite(
    ): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> {


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FavoritePagingDataSource(apiServices) }).flow
    }
}