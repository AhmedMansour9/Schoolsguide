package com.eaapps.schoolsguide.features.login

import android.content.res.Resources
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.domain.model.LoginModel
import com.eaapps.schoolsguide.domain.model.SocialModel
import com.eaapps.schoolsguide.domain.usecase.LoginBySocialUseCase
import com.eaapps.schoolsguide.domain.usecase.LoginUseCase
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.StateFlows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginBySocialUseCase: LoginBySocialUseCase,
    private val resources: Resources
) : ViewModel() {

    lateinit var navigator: LoginNavigator
    val loginModel: LoginModel = LoginModel()
    val socialModel = SocialModel()

    internal val loginStateFlow = StateFlows<AuthResponse.AuthData>(viewModelScope)
    internal val loginByGoogleStateFlow = StateFlows<AuthResponse.AuthData>(viewModelScope)

    val inputEditHelper = ObservableField<HashMap<String, String>>(HashMap())
    private var helperValid = HashMap<String, String>().apply {
        put("email", "")
        put("password", "")
    }

    val registerNow = {
        navigator.registerNow()
    }

    fun loginClicked() {
        inputEditHelper.set(helperValid)
        inputEditHelper.notifyChange()
        if (loginUseCase.isValid(loginModel)) {
            viewModelScope.launch {
                try {
                    loginStateFlow.setValue(Resource.Loading())
                    val result = loginUseCase.execute(loginModel)
                    loginStateFlow.setValue(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            inputEditHelper.set(loginUseCase.validMessage(resources,loginModel))
            inputEditHelper.notifyChange()
        }
    }

    fun loginBySocial() {
        viewModelScope.launch {
            try {
                loginByGoogleStateFlow.setValue(Resource.Loading())
                val result = loginBySocialUseCase.execute(socialModel)
                loginByGoogleStateFlow.setValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun forgetClicked() = navigator.forgetPassword()

}