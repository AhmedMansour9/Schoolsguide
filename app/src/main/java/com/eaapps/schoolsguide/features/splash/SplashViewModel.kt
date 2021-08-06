package com.eaapps.schoolsguide.features.splash

import androidx.lifecycle.ViewModel
import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.domain.usecase.ProfileFatherStoredUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val profileFatherStoredUseCase: ProfileFatherStoredUseCase) :
    ViewModel() {


    fun flowProfile(): Flow<Resource<DataAuth>> = profileFatherStoredUseCase.execute()
}