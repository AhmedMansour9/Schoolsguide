package com.eaapps.schoolsguide.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.usecase.LoadAllFeaturedUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadAllRecommendedUseCase
import com.eaapps.schoolsguide.domain.usecase.LoadTypedSchoolUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadAllRecommendedUseCase: LoadAllRecommendedUseCase,
    private val loadAllFeaturedUseCase: LoadAllFeaturedUseCase,
    private val loadTypedSchoolUseCase: LoadTypedSchoolUseCase
) : ViewModel() {

    suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllRecommendedUseCase.execute().cachedIn(viewModelScope)


    suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadAllFeaturedUseCase.execute().cachedIn(viewModelScope)


    suspend fun loadTypedSchoolById(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        loadTypedSchoolUseCase.execute(typeId).cachedIn(viewModelScope)


}