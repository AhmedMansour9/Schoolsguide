package com.eaapps.schoolsguide

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuideApp : Application() {
    override fun onCreate() {
        super.onCreate()
       // FacebookSdk.sdkInitialize(applicationContext);
        FacebookSdk.setAutoLogAppEventsEnabled(true)
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

    }

}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
