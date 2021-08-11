package com.eaapps.schoolsguide.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.eaapps.schoolsguide.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    public fun provideAppContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> {
        return application.dataStore

    }

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.resources

}