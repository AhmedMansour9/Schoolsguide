package com.eaapps.schoolsguide.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.eaapps.schoolsguide.data.dataStore.Keys
import com.eaapps.schoolsguide.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
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
    fun provideSharedPreference(application: Application): SharedPreferences =
        application.getSharedPreferences(
            "session",
            MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun provideResources(
        application: Application,
        sharedPreferences: SharedPreferences
    ): Resources {
        val conf = Configuration(application.resources.configuration)
        val lang = sharedPreferences.getString(Keys.LANGUAGE, "ar")
        conf.setLocale(Locale(lang ?: "ar"))
        val localizedContext: Context = application.createConfigurationContext(conf)
        return localizedContext.resources
    }

}