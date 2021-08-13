package com.eaapps.schoolsguide.di

import android.content.Context
import com.eaapps.schoolsguide.BuildConfig
import com.eaapps.schoolsguide.data.network.ApiServices
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.utils.NetworkChecker
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CONNECT_TIMEOUT_IN_SECONDS = 10
    private const val READ_TIMEOUT_IN_SECONDS = 60
    private const val WRITE_TIMEOUT_IN_SECONDS = 60

    val interceptorConnectivityManager: (Context) -> Interceptor = { context ->
        Interceptor {
            if (!NetworkChecker.isNetworkConnected(context))
                throw IOException("No connectivity exception")
            it.proceed(it.request().newBuilder().build())

        }
    }

    val interceptorAuthorization: (String?) -> Interceptor = { s ->
        Interceptor {
            val newRequest = it.request().newBuilder()
            s?.apply {
                newRequest.addHeader("Authorization", "Bearer $this")
            }
            it.proceed(newRequest.build())
        }
    }

    @Provides
    fun provideOkHttpClient(
        context: Context,
        dataStoreRepository: DataStoreRepository
    ): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(interceptorConnectivityManager(context))
            .addInterceptor(interceptorAuthorization(dataStoreRepository.loadSessionToken()))
            .build()

    @Provides
    fun providerApiService(okHttpClient: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build().create(ApiServices::class.java)

}