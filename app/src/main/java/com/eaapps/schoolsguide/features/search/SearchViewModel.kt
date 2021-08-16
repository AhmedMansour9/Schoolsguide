package com.eaapps.schoolsguide.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.*
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadAllRecommendedUseCase: LoadAllRecommendedUseCase,
    private val loadAllFeaturedUseCase: LoadAllFeaturedUseCase,
    private val loadTypedSchoolUseCase: LoadTypedSchoolUseCase,
    private val loadFilterSchoolsUseCase: LoadFilterSchoolsUseCase,
    private val loadSchoolTypeUseCase: LoadSchoolTypeUseCase,
    private val programSchoolUseCase: ProgramSchoolUseCase,
    private val gradesSchoolUseCase: GradesSchoolUseCase,
    private val citiesUseCase: CitiesUseCase
) : ViewModel() {

    init {
        loadCities()
        loadSchoolType()
        loadSchoolGrades()
        loadSchoolProgram()
    }

    var filterModel = FilterModel()

    private val _schoolTypeStateFlow: MutableStateFlow<Resource<List<TypeResponse.TypeData>>> =
        MutableStateFlow(Resource.Nothing())
    val schoolTypeStateFlow: StateFlow<Resource<List<TypeResponse.TypeData>>> = _schoolTypeStateFlow

    private val _schoolProgramStateFlow: MutableStateFlow<Resource<List<ProgramsResponse.Programs>>> =
        MutableStateFlow(Resource.Nothing())
    val schoolProgramStateFlow: StateFlow<Resource<List<ProgramsResponse.Programs>>> =
        _schoolProgramStateFlow

    private val _schoolGradesStateFlow: MutableStateFlow<Resource<List<GradesResponse.Grades>>> =
        MutableStateFlow(Resource.Nothing())
    val schoolGradesStateFlow: StateFlow<Resource<List<GradesResponse.Grades>>> =
        _schoolGradesStateFlow

    private var _citiesStateFlow: MutableStateFlow<Resource<List<CityResponse.City>>> = MutableStateFlow(Resource.Nothing())
    val citiesStateFlow: StateFlow<Resource<List<CityResponse.City>>> = _citiesStateFlow


    suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllRecommendedUseCase.execute().cachedIn(viewModelScope)


    suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllFeaturedUseCase.execute().cachedIn(viewModelScope)


    suspend fun loadTypedSchoolById(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadTypedSchoolUseCase.execute(typeId).cachedIn(viewModelScope)


    suspend fun loadSchoolFilters(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadFilterSchoolsUseCase.execute(filterModel).cachedIn(viewModelScope)

    private fun loadSchoolType() {
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

    private fun loadSchoolProgram() {
        viewModelScope.launch {
            try {
                _schoolProgramStateFlow.emit(Resource.Loading())
                val result = programSchoolUseCase.execute()
                _schoolProgramStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadSchoolGrades() {
        viewModelScope.launch {
            try {
                _schoolGradesStateFlow.emit(Resource.Loading())
                val result = gradesSchoolUseCase.execute()
                _schoolGradesStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            try {
                _citiesStateFlow.emit(Resource.Loading())
                val result = citiesUseCase.execute()
                _citiesStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}