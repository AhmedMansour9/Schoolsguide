package com.eaapps.schoolsguide.di

import com.eaapps.schoolsguide.data.dataStore.DataStoreRepositoryImpl
import com.eaapps.schoolsguide.data.network.*
import com.eaapps.schoolsguide.domain.repository.*
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


    @Provides
    @Singleton
    fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository =
        profileRepositoryImpl


    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository =
        favoriteRepositoryImpl


}