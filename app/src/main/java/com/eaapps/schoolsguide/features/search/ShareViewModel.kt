package com.eaapps.schoolsguide.features.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.FilterMapUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadFilterSchoolsUseCase
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val loadFilterSchoolsUseCase: LoadFilterSchoolsUseCase,
    private val filterMapUseCase: FilterMapUseCase
) : ViewModel() {

    var filterModel = FilterModel()
    var mapSearch = false
    val filterFire:MutableLiveData<Resource<Filter>> = MutableLiveData(Resource.Nothing())
    val filterMapFire:MutableLiveData<Resource<Filter>> = MutableLiveData(Resource.Nothing())
    internal val mapStateFlow = StateFlows<List<SchoolResponse.SchoolData.DataSchool>>(viewModelScope)

    suspend fun loadSchoolFilters(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadFilterSchoolsUseCase.execute(filterModel).cachedIn(viewModelScope)

    fun loadSchoolMap() {
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
}

enum class Filter {
    APPLY_FILTER,
    CLEAR_FILTER
}