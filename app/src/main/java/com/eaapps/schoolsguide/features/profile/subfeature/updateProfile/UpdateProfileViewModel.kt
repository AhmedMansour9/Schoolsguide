package com.eaapps.schoolsguide.features.profile.subfeature.updateProfile

import android.content.res.Resources
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.UpdateProfileModel
import com.eaapps.schoolsguide.domain.usecase.CitiesUseCase
import com.eaapps.schoolsguide.domain.usecase.UpdateFatherProfileUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val updateFatherProfileUseCase: UpdateFatherProfileUseCase,
    private val getCitiesUseCase: CitiesUseCase,
    private val resources: Resources
) : ViewModel() {

    init {
        loadCities()
    }

    private val _updateProfileStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val updateProfileStateFlow: StateFlow<Resource<ResponseEntity>> = _updateProfileStateFlow

    val inputEditError = ObservableField<HashMap<String, String>>(HashMap())

    private var _citiesStateFlow: MutableStateFlow<Resource<List<CityResponse.City>>> =
        MutableStateFlow(Resource.Nothing())
    val citiesStateFlow: StateFlow<Resource<List<CityResponse.City>>> = _citiesStateFlow

    var updateProfileModel: UpdateProfileModel = UpdateProfileModel()


    private var helperValid = HashMap<String, String>().apply {
        put("name", "")
        put("email", "")
        put("phone", "")
        put("city", "")
    }

    private fun loadCities() {
        viewModelScope.launch {
            val result = getCitiesUseCase.execute()
            _citiesStateFlow.emit(result)
        }
    }

    fun updateProfileBtn() {
        inputEditError.set(helperValid)
        inputEditError.notifyChange()
        if (updateFatherProfileUseCase.isValid(updateProfileModel)) {
            viewModelScope.launch {
                try {
                    _updateProfileStateFlow.emit(Resource.Loading())
                    val result = updateFatherProfileUseCase.execute(updateProfileModel)
                    _updateProfileStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditError.set(updateFatherProfileUseCase.validMessage(resources,updateProfileModel))
            inputEditError.notifyChange()
        }
    }


}