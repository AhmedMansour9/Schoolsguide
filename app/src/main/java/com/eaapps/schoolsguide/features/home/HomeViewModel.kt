package com.eaapps.schoolsguide.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.entity.SliderResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.domain.usecase.LoadFeatureUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadRecommendedUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadSchoolTypeUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadSliderUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadSchoolTypeUseCase: LoadSchoolTypeUseCase,
    private val loadSliderUseCase: LoadSliderUseCase,
    private val loadRecommendedUseCase: LoadRecommendedUseCase,
    private val loadFeatureUseCase: LoadFeatureUseCase
) : ViewModel() {

    init {
        loadSchoolType()
        loadSlider()
        loadRecommendedSchool()
        loadFeatureSchool()
    }


    private val _schoolTypeStateFlow: MutableStateFlow<Resource<List<TypeResponse.TypeData>>> =
        MutableStateFlow(Resource.Nothing())
    val schoolTypeStateFlow: StateFlow<Resource<List<TypeResponse.TypeData>>> = _schoolTypeStateFlow


    private val _sliderStateFlow: MutableStateFlow<Resource<List<SliderResponse.SliderData>>> =
        MutableStateFlow(Resource.Nothing())
    val sliderStateFlow: StateFlow<Resource<List<SliderResponse.SliderData>>> = _sliderStateFlow


    private val _recommendedStateFlow: MutableStateFlow<Resource<List<SchoolResponse.SchoolData.DataSchool>>> =
        MutableStateFlow(Resource.Nothing())
    val recommendedStateFlow: StateFlow<Resource<List<SchoolResponse.SchoolData.DataSchool>>> =
        _recommendedStateFlow


    private val _featureStateFlow: MutableStateFlow<Resource<List<SchoolResponse.SchoolData.DataSchool>>> =
        MutableStateFlow(Resource.Nothing())
    val featureStateFlow: StateFlow<Resource<List<SchoolResponse.SchoolData.DataSchool>>> =
        _featureStateFlow


    private fun loadSchoolType(){
        viewModelScope.launch {
            try {
                _schoolTypeStateFlow.emit(Resource.Loading())
                val result = loadSchoolTypeUseCase.execute()
                _schoolTypeStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun loadSlider() {
        viewModelScope.launch {
            try {
                _sliderStateFlow.emit(Resource.Loading())
                val result = loadSliderUseCase.execute()
                _sliderStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadRecommendedSchool() {
        viewModelScope.launch {
            try {
                _recommendedStateFlow.emit(Resource.Loading())
                val result = loadRecommendedUseCase.execute()
                _recommendedStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadFeatureSchool() {
        viewModelScope.launch {
            try {
                _featureStateFlow.emit(Resource.Loading())
                val result = loadFeatureUseCase.execute()
                _featureStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}