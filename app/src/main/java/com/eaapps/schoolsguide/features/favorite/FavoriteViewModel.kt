package com.eaapps.schoolsguide.features.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.usecase.LoadFavoriteUseCase
import com.eaapps.schoolsguide.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val loadFavoriteUseCase: LoadFavoriteUseCase,private val toggleFavoriteUseCase: ToggleFavoriteUseCase) :
    ViewModel() {


    suspend fun loadData(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadFavoriteUseCase.execute().cachedIn(viewModelScope)

    fun toggleFavorite(schoolId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(schoolId)
        }
    }

}


data class UiState(val pagingData: PagingData<SchoolResponse.SchoolData.DataSchool> = PagingData.empty())