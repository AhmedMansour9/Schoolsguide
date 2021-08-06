package com.eaapps.schoolsguide.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.getValueFlow
import com.eaapps.schoolsguide.utils.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    DataStoreRepository {

    override suspend fun saveFatherData(dataAuth: DataAuth) {
        val result = Gson().toJson(dataAuth)
        dataStore.setValue(stringPreferencesKey(Keys.FATHER_PROFILE), result)
    }

    override fun loadFatherFromStoreData(): Flow<Resource<DataAuth>> {
        return dataStore.getValueFlow(stringPreferencesKey(Keys.FATHER_PROFILE), "")
            .map {
                val type = object : TypeToken<DataAuth>() {}.type
                val dataAuth: DataAuth? =
                    Gson().fromJson(it, type)
                Resource.Success(dataAuth)
            }
    }
}