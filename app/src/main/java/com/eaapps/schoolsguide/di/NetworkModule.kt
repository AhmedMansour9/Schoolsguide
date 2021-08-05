package com.eaapps.schoolsguide.di

import android.content.Context
import com.eaapps.schoolsguide.BuildConfig
import com.eaapps.schoolsguide.data.network.ApiServices
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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val interceptorConnectivityManager: (Context) -> Interceptor = { context ->
        Interceptor {
            if (!NetworkChecker.isNetworkConnected(context))
                throw IOException("No connectivity exception")
            it.proceed(it.request().newBuilder().build())

        }
    }

    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(interceptorConnectivityManager(context))
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