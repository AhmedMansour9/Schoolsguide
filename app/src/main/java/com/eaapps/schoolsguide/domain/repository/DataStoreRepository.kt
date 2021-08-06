package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveFatherData(dataAuth: DataAuth)

    fun loadFatherFromStoreData():Flow<Resource<DataAuth>>


}