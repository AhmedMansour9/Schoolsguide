package com.eaapps.schoolsguide.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.domain.usecase.GetProfileFatherUseCase
import com.eaapps.schoolsguide.domain.usecase.ProfileFatherStoredUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val profileFatherStoredUseCase: ProfileFatherStoredUseCase,
    private val getProfileFatherUseCase: GetProfileFatherUseCase
) :
    ViewModel() {

    init {
        loadProfile()
    }

    lateinit var accessToken:String

    private val _profileStateFlow: MutableStateFlow<Resource<AuthResponse.AuthData>> =
        MutableStateFlow(Resource.Nothing())
    val profileStateFlow: StateFlow<Resource<AuthResponse.AuthData>> = _profileStateFlow


      fun loadProfile() {
        viewModelScope.launch {
            profileFatherStoredUseCase.execute().collect {
                if (it is Resource.Success) {
                    val data = it.data
                    data?.apply {
                        try {
                            accessToken = access_token
                            _profileStateFlow.emit(Resource.Loading())
                            val result = getProfileFatherUseCase.execute(access_token)
                            _profileStateFlow.emit(result)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }




}