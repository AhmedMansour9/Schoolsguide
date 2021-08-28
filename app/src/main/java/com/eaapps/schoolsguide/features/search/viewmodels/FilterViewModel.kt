package com.eaapps.schoolsguide.features.search.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.domain.usecase.CitiesUseCase
import com.eaapps.schoolsguide.domain.usecase.GradesSchoolUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadSchoolTypeUseCase
import com.eaapps.schoolsguide.domain.usecase.ProgramSchoolUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val programSchoolUseCase: ProgramSchoolUseCase,
    private val gradesSchoolUseCase: GradesSchoolUseCase,
    private val citiesUseCase: CitiesUseCase,
    private val loadSchoolTypeUseCase: LoadSchoolTypeUseCase,

    ) : ViewModel() {

    fun load() {
        loadCities()
        loadSchoolType()
        loadSchoolGrades()
        loadSchoolProgram()
    }

    internal val schoolProgramLiveData =
        MutableLiveData<Resource<List<ProgramsResponse.Programs>>>()

    internal val schoolGradesLiveData = MutableLiveData<Resource<List<GradesResponse.Grades>>>()

    internal val citiesLiveData = MutableLiveData<Resource<List<CityResponse.City>>>()

    internal val schoolTypeLiveData = MutableLiveData<Resource<List<TypeResponse.TypeData>>>()

    private fun loadSchoolProgram() {
        viewModelScope.launch {
            try {
                schoolProgramLiveData.value = Resource.Loading()
                val result = programSchoolUseCase.execute()
                schoolProgramLiveData.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadSchoolGrades() {
        viewModelScope.launch {
            try {
                schoolGradesLiveData.value = Resource.Loading()
                val result = gradesSchoolUseCase.execute()
                schoolGradesLiveData.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                citiesLiveData.value = Resource.Loading()
                val result = citiesUseCase.execute()
                citiesLiveData.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun loadSchoolType() {
        viewModelScope.launch {
            try {
                schoolTypeLiveData.value = Resource.Loading()
                val result = loadSchoolTypeUseCase.execute()
                schoolTypeLiveData.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}