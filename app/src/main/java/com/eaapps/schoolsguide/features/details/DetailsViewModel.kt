package com.eaapps.schoolsguide.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.usecase.SchoolDetailsUseCase
import com.eaapps.schoolsguide.domain.usecase.ToggleFollowUseCase
import com.eaapps.schoolsguide.domain.usecase.ToggleRecommendedUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val toggleFollowUseCase: ToggleFollowUseCase,
    private val toggleRecommendedUseCase: ToggleRecommendedUseCase,
    private val schoolDetailsUseCase: SchoolDetailsUseCase
) : ViewModel() {

    private val _schoolDetailsStateFlow: MutableStateFlow<Resource<SchoolResponse.SchoolData.DataSchool>> =
        MutableStateFlow(Resource.Nothing())
    val schoolDetailsStateFlow: StateFlow<Resource<SchoolResponse.SchoolData.DataSchool>> =
        _schoolDetailsStateFlow

    private val _toggleFollowStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val toggleFollowStateFlow: StateFlow<Resource<ResponseEntity>> =
        _toggleFollowStateFlow

    private val _toggleRecommendedStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val toggleRecommendedStateFlow: StateFlow<Resource<ResponseEntity>> =
        _toggleRecommendedStateFlow

    fun loadSchoolDetails(schoolId: Int) {
        viewModelScope.launch {
            try {
                _schoolDetailsStateFlow.emit(Resource.Loading())
                val result = schoolDetailsUseCase.execute(schoolId)
                _schoolDetailsStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFollow(schoolId: Int) {
        viewModelScope.launch {
            try {
                _toggleFollowStateFlow.emit(Resource.Loading())
                val result = toggleFollowUseCase.execute(schoolId)
                _toggleFollowStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleRecommended(schoolId: Int) {
        viewModelScope.launch {
            try {
                _toggleRecommendedStateFlow.emit(Resource.Loading())
                val result = toggleRecommendedUseCase.execute(schoolId)
                _toggleRecommendedStateFlow.emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}