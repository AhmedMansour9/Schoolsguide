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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    lateinit var navigator: LoginNavigator
    var loginModel: LoginModel = LoginModel()

    val inputEditError = ObservableField<HashMap<String, String>>(HashMap())
    private var errorHashMap = HashMap<String, String>()

    private val _loginStateFlow = MutableStateFlow<Resource<DataAuth>>(Resource.Nothing())
    val loginStateFlow: StateFlow<Resource<DataAuth>> = _loginStateFlow


    val registerNow = {
        navigator.registerNow()
    }

    fun loginClicked() {
        if (loginUseCase.isValid(loginModel)){
             viewModelScope.launch {
                 _loginStateFlow.emit(Resource.Loading())
                 val result =  loginUseCase.execute(loginModel)

                 _loginStateFlow.emit(result)
            }

        }else{
            if (loginModel.email.isBlank()) {
                errorHashMap = HashMap()
                errorHashMap["email"] = "Please Enter Email Address"
                inputEditError.set(errorHashMap)
                inputEditError.notifyChange()
                return
            }

            if (loginModel.password.isBlank()) {
                errorHashMap = HashMap()
                errorHashMap["password"] = "Please Enter Password"
                inputEditError.set(errorHashMap)
                inputEditError.notifyChange()
                return
            }

        }
    }

    fun forgetClicked(){

    }


}