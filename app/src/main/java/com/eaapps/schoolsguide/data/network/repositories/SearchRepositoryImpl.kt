package com.eaapps.schoolsguide.data.network.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.FilterRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.network.ApiServices
import com.eaapps.schoolsguide.data.network.dataSources.FeaturedPagingSource
import com.eaapps.schoolsguide.data.network.dataSources.FilterSchoolPagingSource
import com.eaapps.schoolsguide.data.network.dataSources.RecommendedPagingDataSource
import com.eaapps.schoolsguide.data.network.dataSources.TypedSchoolPagingSource
import com.eaapps.schoolsguide.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    SearchRepository {
    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    override suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { RecommendedPagingDataSource(apiServices) }).flow


    override suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FeaturedPagingSource(apiServices) }).flow


    override suspend fun loadAllTypedSchool(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { TypedSchoolPagingSource(apiServices, typeId) }).flow

    override suspend fun loadAllSchoolsByFilter(filterRequestEntity: FilterRequestEntity): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FilterSchoolPagingSource(apiServices, filterRequestEntity) }).flow



}