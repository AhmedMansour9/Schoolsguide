package com.eaapps.schoolsguide.features.search.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.FilterMapUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadFilterSchoolsUseCase
import com.eaapps.schoolsguide.domain.usecase.ToggleFavoriteUseCase
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val loadFilterSchoolsUseCase: LoadFilterSchoolsUseCase,
    private val filterMapUseCase: FilterMapUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase

    ) : ViewModel() {

    var filterModel = FilterModel()
    val filterFire: MutableLiveData<Resource<Filter>> = MutableLiveData(Resource.Nothing())
    val filterMapFire: MutableLiveData<Resource<Filter>> = MutableLiveData(Resource.Nothing())
    internal val mapStateFlow = StateFlows<List<SchoolResponse.SchoolData.DataSchool>>(viewModelScope)
    internal val toggleFavoriteStateFlow = StateFlows<ResponseEntity>(viewModelScope)

    suspend fun loadSchoolFilters(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadFilterSchoolsUseCase.execute(filterModel).cachedIn(viewModelScope)

    fun filterSchoolByMap() {
        viewModelScope.launch {
            try {
                mapStateFlow.setValue(Resource.Loading())
                val result = filterMapUseCase.execute(filterModel)
                mapStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun filterDefault() {
        filterModel = FilterModel()
        filterMapFire.value = Resource.Nothing()
        viewModelScope.launch {
            mapStateFlow.setValue(Resource.Nothing())
        }
    }

    fun filterSearchDefault() {
        filterModel = FilterModel()
        filterFire.value = Resource.Nothing()
    }

    fun toggleFavorite(schoolId: Int) {
        viewModelScope.launch {
            try {
                toggleFavoriteStateFlow.setValue(Resource.Loading())
                val result = toggleFavoriteUseCase.execute(schoolId)
                toggleFavoriteStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

enum class Filter {
    APPLY_FILTER,
    CLEAR_FILTER
}