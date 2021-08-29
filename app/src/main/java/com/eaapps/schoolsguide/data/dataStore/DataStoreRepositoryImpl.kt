package com.eaapps.schoolsguide.data.dataStore

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.eaapps.schoolsguide.data.dataStore.Keys.LANGUAGE
import com.eaapps.schoolsguide.data.dataStore.Keys.SESSION_TOKEN
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.getValueFlow
import com.eaapps.schoolsguide.utils.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val sharedPreferences: SharedPreferences
) :
    DataStoreRepository {

    override suspend fun saveFatherData(authDataResponse: AuthResponse.AuthData) {
        val result = Gson().toJson(authDataResponse)
        dataStore.setValue(stringPreferencesKey(Keys.FATHER_PROFILE), result)
    }

    override fun loadFatherFromStoreData(): Flow<Resource<AuthResponse.AuthData>> {
        return dataStore.getValueFlow(stringPreferencesKey(Keys.FATHER_PROFILE), "")
            .map {
                val type = object : TypeToken<AuthResponse.AuthData>() {}.type
                val authDataResponse: AuthResponse.AuthData? =
                    Gson().fromJson(it, type)
                Resource.Success(authDataResponse)
            }
    }

    @SuppressLint("CommitPrefEdits")
    override fun saveTokenSession(accessToken: String) {
        sharedPreferences.edit().apply {
            putString(SESSION_TOKEN, accessToken)
            apply()
        }
    }

    override fun loadSessionToken(): String? {
        return sharedPreferences.let {
            sharedPreferences.getString(SESSION_TOKEN, "")
        }
    }

    override fun saveLanguage(lang: String){
        sharedPreferences.edit().apply {
            putString(LANGUAGE, lang)
            apply()
        }
    }

    override fun loadLanguage(): String? {
        return sharedPreferences.let {
            sharedPreferences.getString(LANGUAGE, "en")
        }
    }
}