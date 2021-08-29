package com.eaapps.schoolsguide.utils

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import com.eaapps.schoolsguide.di.InitializerEntryPoint
import com.eaapps.schoolsguide.domain.usecase.LoadLanguageUseCase
import java.util.*
import javax.inject.Inject

class LanguageContextWrapper @Inject constructor(base: Context) : ContextWrapper(base) {

    @Inject
    lateinit var loadLanguageUseCase: LoadLanguageUseCase

    init {
        InitializerEntryPoint.Resolve.resolve(base).inject(this)
        updateLocale(base)
    }

    private fun updateLocale(context: Context) {
        val localeCode = loadLanguageUseCase.let {
            loadLanguageUseCase.execute()!!
        } ?: "ar"

        val locale = Locale(localeCode)
        val resource = context.resources
        val configuration = resource.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale) // 2
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.locale = locale
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(configuration) // 6
        } else {
            resources.updateConfiguration(configuration, resources.displayMetrics) // 7
        }
    }
}