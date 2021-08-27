package com.eaapps.schoolsguide.features.profile.addSchool

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.AddSchoolModel
import com.eaapps.schoolsguide.domain.usecase.AddSchoolUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSchoolViewModel @Inject constructor(private val addSchoolUseCase: AddSchoolUseCase) :
    ViewModel() {

    private val _addSchoolStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val addSchoolStateFlow: StateFlow<Resource<ResponseEntity>> = _addSchoolStateFlow

    var addSchoolModel: AddSchoolModel = AddSchoolModel()
    val inputEditHelper = ObservableField<HashMap<String, String>>(HashMap())

    private var helperValid = HashMap<String, String>().apply {
        put("name", "")
        put("email", "")
        put("phone", "")
    }

    fun addSchoolBtn() {
        inputEditHelper.set(helperValid)
        inputEditHelper.notifyChange()
        if (addSchoolUseCase.isValid(addSchoolModel)) {
            viewModelScope.launch {
                try {
                    _addSchoolStateFlow.emit(Resource.Loading())
                    val result = addSchoolUseCase.execute(addSchoolModel)
                    _addSchoolStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(addSchoolUseCase.validMessage(addSchoolModel))
            inputEditHelper.notifyChange()
        }
    }
}