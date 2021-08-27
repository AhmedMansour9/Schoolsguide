package com.eaapps.schoolsguide.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.usecase.GetProfileFatherUseCase
import com.eaapps.schoolsguide.domain.usecase.LogoutFatherUseCase
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.NetworkChecker
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProfileFatherUseCase: GetProfileFatherUseCase,
    private val logoutFatherUseCase: LogoutFatherUseCase
) : ViewModel() {

    internal val profileStateFlow = StateFlows<AuthResponse.AuthData>(viewModelScope)
    internal val logoutStateFlow = StateFlows<ResponseEntity>(viewModelScope)

    fun loadProfile() {
        viewModelScope.launch {
            try {
                profileStateFlow.setValue(Resource.Loading())
                val result = getProfileFatherUseCase.execute()
                profileStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logoutFather() {
        viewModelScope.launch {
            try {
                logoutStateFlow.setValue(Resource.Loading())
                profileStateFlow.setValue(Resource.Nothing())
                val result = logoutFatherUseCase.execute()
                logoutStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}