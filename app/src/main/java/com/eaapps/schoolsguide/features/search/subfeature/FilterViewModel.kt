package com.eaapps.schoolsguide.features.search.subfeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.CitiesUseCase
import com.eaapps.schoolsguide.domain.usecase.FilterMapUseCase
import com.eaapps.schoolsguide.domain.usecase.GradesSchoolUseCase
import com.eaapps.schoolsguide.domain.usecase.ProgramSchoolUseCase
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val programSchoolUseCase: ProgramSchoolUseCase,
    private val gradesSchoolUseCase: GradesSchoolUseCase,
    private val citiesUseCase: CitiesUseCase
 ) : ViewModel() {

    init {
        loadCities()
        loadSchoolGrades()
        loadSchoolProgram()
    }

    internal val schoolProgramStateFlow = StateFlows<List<ProgramsResponse.Programs>>(viewModelScope)

    internal val schoolGradesStateFlow = StateFlows<List<GradesResponse.Grades>>(viewModelScope)

    internal val citiesStateFlow = StateFlows<List<CityResponse.City>>(viewModelScope)

    private fun loadSchoolProgram() {
        viewModelScope.launch {
            try {
                schoolProgramStateFlow.setValue(Resource.Loading())
                val result = programSchoolUseCase.execute()
                schoolProgramStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadSchoolGrades() {
        viewModelScope.launch {
            try {
                schoolGradesStateFlow.setValue(Resource.Loading())
                val result = gradesSchoolUseCase.execute()
                schoolGradesStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                citiesStateFlow.setValue(Resource.Loading())
                val result = citiesUseCase.execute()
                citiesStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }







}