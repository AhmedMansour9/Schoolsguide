package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.repository.CityRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CityRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    CityRepository {

    override suspend fun getCities(): Resource<List<String>> = withContext(Dispatchers.IO) {
        safeCall(call = {
            val result = apiServices.getCitiesAsync().await()
            if (result.isSuccessful) {
                Resource.Success(result.body()?.data?.let {
                    val listCitiesStr = ArrayList<String>()
                    it.forEach {
                        listCitiesStr.add(it.name)
                    }
                    listCitiesStr
                })
            } else {
                val type = object : TypeToken<ResponseEntity>() {}.type
                val responseFailure: ResponseEntity? =
                    Gson().fromJson(result.errorBody()!!.charStream(), type)
                Resource.Error(result.code(), responseFailure?.message ?: result.message())
            }

        }, "Exception occurred!")
    }
}