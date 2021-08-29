package com.eaapps.schoolsguide.di

import android.content.Context
import com.eaapps.schoolsguide.utils.LanguageContextWrapper
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    object Resolve {
        fun resolve(context: Context?): InitializerEntryPoint {
            return EntryPointAccessors.fromApplication(context!!, InitializerEntryPoint::class.java)
        }
    }

    fun inject(languageContextWrapper: LanguageContextWrapper?)
}