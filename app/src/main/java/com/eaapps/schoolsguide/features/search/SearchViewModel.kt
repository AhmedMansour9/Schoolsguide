package com.eaapps.schoolsguide.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.FilterModel
import com.eaapps.schoolsguide.domain.usecase.*
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
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
    private val loadSchoolTypeUseCase: LoadSchoolTypeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,

) : ViewModel() {

    init {
        loadSchoolType()
    }

    var filterModel = FilterModel()

    internal val schoolTypeStateFlow = StateFlows<List<TypeResponse.TypeData>>(viewModelScope)
    internal val toggleFavoriteStateFlow = StateFlows<ResponseEntity>(viewModelScope)

    suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllRecommendedUseCase.execute().cachedIn(viewModelScope)

    suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllFeaturedUseCase.execute().cachedIn(viewModelScope)

    suspend fun loadTypedSchoolById(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadTypedSchoolUseCase.execute(typeId).cachedIn(viewModelScope)

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