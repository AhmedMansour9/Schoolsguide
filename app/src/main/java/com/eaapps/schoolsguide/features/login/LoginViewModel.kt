package com.eaapps.schoolsguide.features.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.domain.model.LoginModel
import com.eaapps.schoolsguide.domain.usecase.LoginUseCase
import com.eaapps.schoolsguide.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    lateinit var navigator: LoginNavigator
    var loginModel: LoginModel = LoginModel()

    val inputEditError = ObservableField<HashMap<String, String>>(HashMap())
    private var helperValid = HashMap<String, String>().apply {
        put("email", "")
        put("password", "")
    }

    private val _loginStateFlow = MutableStateFlow<Resource<DataAuth>>(Resource.Nothing())
    val loginStateFlow: StateFlow<Resource<DataAuth>> = _loginStateFlow

    val registerNow = {
        navigator.registerNow()

    }

    private fun login() {
        inputEditError.set(helperValid)
        inputEditError.notifyChange()
        if (loginUseCase.isValid(loginModel)) {
            viewModelScope.launch {
                try {
                    _loginStateFlow.emit(Resource.Loading())
                    val result = loginUseCase.execute(loginModel)
                    _loginStateFlow.emit(result)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        } else {
            inputEditError.set(loginUseCase.validMessage(loginModel))
            inputEditError.notifyChange()
        }
    }

    fun loginClicked() = login()

    fun forgetClicked() {
    }


}