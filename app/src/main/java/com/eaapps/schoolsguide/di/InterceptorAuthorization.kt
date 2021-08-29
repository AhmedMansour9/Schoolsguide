package com.eaapps.schoolsguide.di

import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterceptorAuthorization @Inject constructor(dataStoreRepository: DataStoreRepository) :
    Interceptor {
    var token: String = ""
    var language = "ar"

    init {
        token = dataStoreRepository.loadSessionToken()!!
        language = dataStoreRepository.loadLanguage()!!
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        token.isNotBlank().apply {
            newRequest.addHeader("Authorization", "Bearer $token")
        }

        language.isNotBlank().apply {
            newRequest.addHeader("X-localization", language)
        }

        return chain.proceed(newRequest.build())
    }


}