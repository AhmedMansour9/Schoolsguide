package com.eaapps.schoolsguide.features.forgetPassword

import android.content.res.Resources
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.domain.usecase.CreateNewPasswordUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val createNewPasswordUseCase: CreateNewPasswordUseCase,
    private val resources: Resources

) :
    ViewModel() {

    val inputEditHelp = ObservableField<HashMap<String, String>>(HashMap())
    private var helperValid = HashMap<String, String>().apply {
        put("email", "")
    }

    var email: String = ""

    private val _createStateFlow = MutableStateFlow<Resource<AuthResetResponse>>(Resource.Nothing())
    val createStateFlow: StateFlow<Resource<AuthResetResponse>> = _createStateFlow

    fun sendInstructionsBtn() {
        inputEditHelp.set(helperValid)
        inputEditHelp.notifyChange()
        if (createNewPasswordUseCase.isValid(email)) {
            viewModelScope.launch {
                try {
                    _createStateFlow.emit(Resource.Loading())
                    val result = createNewPasswordUseCase.execute(email)
                    _createStateFlow.emit(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelp.set(createNewPasswordUseCase.validMessage(resources,email))
            inputEditHelp.notifyChange()
        }
    }


}