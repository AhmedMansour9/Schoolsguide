package com.eaapps.schoolsguide.features.details

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.domain.model.BookSchoolModel
import com.eaapps.schoolsguide.domain.model.InquiryModel
import com.eaapps.schoolsguide.domain.model.JoinDiscountModel
import com.eaapps.schoolsguide.domain.usecase.*
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val toggleFollowUseCase: ToggleFollowUseCase,
    private val toggleRecommendedUseCase: ToggleRecommendedUseCase,
    private val schoolDetailsUseCase: SchoolDetailsUseCase,
    private val addInquiryUseCase: AddInquiryUseCase,
    private val schoolBookSchoolUseCase: BookSchoolUseCase,
    private val joinDiscountSchoolUseCase: JoinDiscountSchoolUseCase,
    private val gradesSchoolUseCase: GradesSchoolUseCase
) : ViewModel() {

    init {
        Log.d("asdasssa", "asd: ")
        loadSchoolGrades()
    }

    internal val schoolDetailsFlow =
        StateFlows<SchoolResponse.SchoolData.DataSchool>(viewModelScope)
    internal val toggleFollowFlow = StateFlows<ResponseEntity>(viewModelScope)
    internal val toggleRecommendedFlow = StateFlows<ResponseEntity>(viewModelScope)
    internal val sendInquiryFlow = StateFlows<ResponseEntity>(viewModelScope)
    internal val bookSchoolFlow = StateFlows<ResponseEntity>(viewModelScope)
    internal val joinDiscountFlow = StateFlows<ResponseEntity>(viewModelScope)
    internal val schoolGradesFlow = StateFlows<List<GradesResponse.Grades>>(viewModelScope)

    val inquiryModel = InquiryModel()
    val bookSchoolModel = BookSchoolModel()
    val joinDiscountModel = JoinDiscountModel()

    val inputEditHelper = ObservableField<HashMap<String, String>>(HashMap())
    private var helperValid = HashMap<String, String>().apply {
        put("typeMessage", "")
        put("replay_message", "")
        put("replay_time", "")
    }

    fun toggleFollow(schoolId: Int) {
        viewModelScope.launch {
            try {
                toggleFollowFlow.setValue(Resource.Loading())
                val result = toggleFollowUseCase.execute(schoolId)
                toggleFollowFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleRecommended(schoolId: Int) {
        viewModelScope.launch {
            try {
                toggleRecommendedFlow.setValue(Resource.Loading())
                val result = toggleRecommendedUseCase.execute(schoolId)
                toggleRecommendedFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendInquiry() {
        inputEditHelper.set(helperValid)
        inputEditHelper.notifyChange()
        if (addInquiryUseCase.isValid(inquiryModel)) {
            viewModelScope.launch {
                try {
                    sendInquiryFlow.setValue(Resource.Loading())
                    val result = addInquiryUseCase.execute(inquiryModel)
                    sendInquiryFlow.setValue(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(addInquiryUseCase.validMessage(inquiryModel))
            inputEditHelper.notifyChange()
        }
    }

    fun bookSchoolNow() {
//        inputEditHelper.set(helperValid)
//        inputEditHelper.notifyChange()
        if (schoolBookSchoolUseCase.isValid(bookSchoolModel)) {
            viewModelScope.launch {
                try {
                    bookSchoolFlow.setValue(Resource.Loading())
                    val result = schoolBookSchoolUseCase.execute(bookSchoolModel)
                    bookSchoolFlow.setValue(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(schoolBookSchoolUseCase.validMessage(bookSchoolModel))
            inputEditHelper.notifyChange()
        }
    }

    fun joinDiscount() {
        //        inputEditHelper.set(helperValid)
//        inputEditHelper.notifyChange()
        if (joinDiscountSchoolUseCase.isValid(joinDiscountModel)) {
            viewModelScope.launch {
                try {
                    joinDiscountFlow.setValue(Resource.Loading())
                    val result = joinDiscountSchoolUseCase.execute(joinDiscountModel)
                    joinDiscountFlow.setValue(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(joinDiscountSchoolUseCase.validMessage(joinDiscountModel))
            inputEditHelper.notifyChange()
        }
    }

    private fun loadSchoolGrades() {
        viewModelScope.launch {
            try {
                schoolGradesFlow.setValue(Resource.Loading())
                val result = gradesSchoolUseCase.execute()
                schoolGradesFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSchoolDetails(schoolId: Int) {
        viewModelScope.launch {
            try {
                schoolDetailsFlow.setValue(Resource.Loading())
                val result: Resource<SchoolResponse.SchoolData.DataSchool> =
                    schoolDetailsUseCase.execute(schoolId)
                schoolDetailsFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}