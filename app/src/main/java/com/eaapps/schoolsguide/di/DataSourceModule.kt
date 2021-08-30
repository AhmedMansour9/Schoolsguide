package com.eaapps.schoolsguide.di

import com.eaapps.schoolsguide.data.dataStore.DataStoreRepositoryImpl
import com.eaapps.schoolsguide.data.network.repositories.AuthRepositoryImpl
import com.eaapps.schoolsguide.data.network.repositories.FatherRepositoryImpl
import com.eaapps.schoolsguide.data.network.repositories.GeneralRepositoryImpl
import com.eaapps.schoolsguide.data.network.repositories.PasswordRepositoryImpl
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
    fun provideFatherRepository(fatherRepositoryImpl: FatherRepositoryImpl): FatherRepository =
        fatherRepositoryImpl

    @Provides
    @Singleton
    fun providePasswordRepository(passwordRepositoryImpl: PasswordRepositoryImpl): PasswordRepository =
        passwordRepositoryImpl

    @Provides
    @Singleton
    fun provideGeneralRepository(generalRepositoryImpl: GeneralRepositoryImpl): GeneralRepository =
        generalRepositoryImpl

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository =
        dataStoreRepositoryImpl

}