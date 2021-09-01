package com.eaapps.schoolsguide.features.profile.subfeature.orderSchool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.SchoolBookingRequestsResponse
import com.eaapps.schoolsguide.domain.usecase.MyOrderSchoolUseCase
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val myOrderSchoolUseCase: MyOrderSchoolUseCase) :
    ViewModel() {

    internal val ordersFlow =
        StateFlows<List<SchoolBookingRequestsResponse.RequestData>>(viewModelScope)
    
    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            try {
                ordersFlow.setValue(Resource.Loading())
                val result = myOrderSchoolUseCase.execute()
                ordersFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}