package com.eaapps.schoolsguide.features.resetPassword

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.domain.model.ResetPasswordModel
import com.eaapps.schoolsguide.domain.usecase.ResetNewPasswordUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(private val resetNewPasswordUseCase: ResetNewPasswordUseCase) :
    ViewModel() {

    val resetPasswordModel = ResetPasswordModel()

    val inputEditHelper = ObservableField<HashMap<String, String>>(HashMap())
    private var helperValid = HashMap<String, String>().apply {
        put("email", "")
        put("password", "")
    }

    private val _resetStateFlow = MutableStateFlow<Resource<AuthResetResponse>>(Resource.Nothing())
    val resetStateFlow: StateFlow<Resource<AuthResetResponse>> = _resetStateFlow

    fun resetPasswordBtn() {
        inputEditHelper.set(helperValid)
        inputEditHelper.notifyChange()
        if (resetNewPasswordUseCase.isValid(resetPasswordModel)) {
            viewModelScope.launch {
                try {
                    _resetStateFlow.emit(Resource.Loading())
                    val result = resetNewPasswordUseCase.execute(resetPasswordModel)
                    _resetStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(resetNewPasswordUseCase.validMessage(resetPasswordModel))
            inputEditHelper.notifyChange()
        }

    }

}