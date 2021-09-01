package com.eaapps.schoolsguide.features.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.usecase.LoadFollowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val loadFollowUseCase: LoadFollowUseCase
) : ViewModel() {

    suspend fun loadData(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadFollowUseCase.execute().cachedIn(viewModelScope)
}
