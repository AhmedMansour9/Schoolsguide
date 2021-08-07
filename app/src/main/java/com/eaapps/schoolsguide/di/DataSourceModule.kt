package com.eaapps.schoolsguide.di

import com.eaapps.schoolsguide.data.dataStore.DataStoreRepositoryImpl
import com.eaapps.schoolsguide.data.network.AuthRepositoryImpl
import com.eaapps.schoolsguide.data.network.CityRepositoryImpl
import com.eaapps.schoolsguide.data.network.HomeRepositoryImpl
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.domain.repository.CityRepository
import com.eaapps.schoolsguide.domain.repository.DataStoreRepository
import com.eaapps.schoolsguide.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository =
        authRepositoryImpl

    @Provides
    @Singleton
    fun provideCityRepository(cityRepositoryImpl: CityRepositoryImpl): CityRepository =
        cityRepositoryImpl


    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository =
        dataStoreRepositoryImpl



    @Provides
    @Singleton
    fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository =
        homeRepositoryImpl



}