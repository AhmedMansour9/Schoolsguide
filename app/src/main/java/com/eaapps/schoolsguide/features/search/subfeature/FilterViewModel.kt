package com.eaapps.schoolsguide.features.search.subfeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.*
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
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

    init {
        loadCities()
        loadSchoolType()
        loadSchoolGrades()
        loadSchoolProgram()

    }

    internal val schoolProgramStateFlow = StateFlows<List<ProgramsResponse.Programs>>(viewModelScope)

    internal val schoolGradesStateFlow = StateFlows<List<GradesResponse.Grades>>(viewModelScope)

    internal val citiesStateFlow = StateFlows<List<CityResponse.City>>(viewModelScope)

    internal val schoolTypeStateFlow = StateFlows<List<TypeResponse.TypeData>>(viewModelScope)

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

    private fun loadSchoolType() {
        viewModelScope.launch {
            try {
                schoolTypeStateFlow.setValue(Resource.Loading())
                val result = loadSchoolTypeUseCase.execute()
                schoolTypeStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }







}