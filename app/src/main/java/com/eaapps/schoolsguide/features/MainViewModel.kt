package com.eaapps.schoolsguide.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.usecase.GetProfileFatherUseCase
import com.eaapps.schoolsguide.domain.usecase.LogoutFatherUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProfileFatherUseCase: GetProfileFatherUseCase,
    private val logoutFatherUseCase: LogoutFatherUseCase
) : ViewModel() {
    private val _profileStateFlow: MutableStateFlow<Resource<AuthResponse.AuthData>> =
        MutableStateFlow(Resource.Nothing())
    private val _logoutStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val logoutStateFlow: StateFlow<Resource<ResponseEntity>> = _logoutStateFlow

    val profileStateFlow: StateFlow<Resource<AuthResponse.AuthData>> = _profileStateFlow

    init {

        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            try {
                _profileStateFlow.emit(Resource.Loading())
                val result = getProfileFatherUseCase.execute()
                _profileStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logoutFather() {
        viewModelScope.launch {
            try {
                _logoutStateFlow.emit(Resource.Loading())
                _profileStateFlow.emit(Resource.Nothing())
                val result = logoutFatherUseCase.execute()
                _logoutStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}