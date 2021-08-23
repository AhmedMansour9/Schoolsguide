package com.eaapps.schoolsguide.di

import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterceptorAuthorization @Inject constructor(private val dataStoreRepository: DataStoreRepository) :
    Interceptor {
    var token: String = ""

    init {
        token = dataStoreRepository.loadSessionToken()!!
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        token.isNotBlank().apply {
            //  newRequest.addHeader("X-localization", "ar")
            newRequest.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(newRequest.build())
    }


}