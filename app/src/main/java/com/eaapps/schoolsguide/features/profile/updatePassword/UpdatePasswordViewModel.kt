package com.eaapps.schoolsguide.features.profile.updatePassword

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.UpdatePasswordModel
import com.eaapps.schoolsguide.domain.usecase.UpdatePasswordUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(private val updatePasswordUseCase: UpdatePasswordUseCase) :
    ViewModel() {

    private val _updatePasswordStateFlow: MutableStateFlow<Resource<ResponseEntity>> =
        MutableStateFlow(Resource.Nothing())
    val updatePasswordStateFlow: StateFlow<Resource<ResponseEntity>> = _updatePasswordStateFlow

    var updatePasswordModel = UpdatePasswordModel()
    val inputEditHelper = ObservableField<HashMap<String, String>>(HashMap())

    private var helperValid = HashMap<String, String>().apply {
        put("oldPassword", "")
        put("newPassword", "")
        put("confirmPassword", "")
    }

    fun updatePasswordBtn() {
        inputEditHelper.set(helperValid)
        inputEditHelper.notifyChange()
        if (updatePasswordUseCase.isValid(updatePasswordModel)) {
            viewModelScope.launch {
                try {
                    _updatePasswordStateFlow.emit(Resource.Loading())
                    val result = updatePasswordUseCase.execute(updatePasswordModel)
                    _updatePasswordStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(updatePasswordUseCase.validMessage(updatePasswordModel))
            inputEditHelper.notifyChange()
        }
    }


}