package com.eaapps.schoolsguide.features.register

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.domain.model.RegisterModel
import com.eaapps.schoolsguide.domain.usecase.CitiesUseCase
import com.eaapps.schoolsguide.domain.usecase.RegisterUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val getCitiesUseCase: CitiesUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    lateinit var registerNavigator: RegisterNavigator

    private var _citiesStateFlow: MutableStateFlow<Resource<List<CityResponse.City>>> =
        MutableStateFlow(Resource.Nothing())
    val citiesStateFlow: StateFlow<Resource<List<CityResponse.City>>> = _citiesStateFlow

    private val _registerStateFlow =
        MutableStateFlow<Resource<AuthResponse.AuthData>>(Resource.Nothing())
    val registerStateFlow: StateFlow<Resource<AuthResponse.AuthData>> = _registerStateFlow

    private var helperValid = HashMap<String, String>().apply {
        put("name", "")
        put("phone", "")
        put("city", "")
        put("email", "")
        put("district", "")
        put("password", "")
        put("confirmPassword", "")
    }

    init {
        loadCities()
    }

    var registerModel = RegisterModel()

    val inputEditError = ObservableField<HashMap<String, String>>(HashMap())

    val conditionClick = {}

    val loginClick = {
        registerNavigator.loginNow()
    }

    fun loadCities() {
        viewModelScope.launch {
            _citiesStateFlow.emit(Resource.Loading())
            val result = getCitiesUseCase.execute()
            _citiesStateFlow.emit(result)
        }
    }

    fun register() {
        inputEditError.set(helperValid)
        inputEditError.notifyChange()
        if (registerUseCase.isValid(registerModel)) {
            viewModelScope.launch {
                try {
                    _registerStateFlow.emit(Resource.Loading())
                    val result = registerUseCase.execute(registerModel)
                    _registerStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditError.set(registerUseCase.validMessage(registerModel))
            inputEditError.notifyChange()
        }
    }

}