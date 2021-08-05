package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.DataAuth
import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.domain.model.LoginModel
import com.eaapps.schoolsguide.domain.model.RegisterModel
import com.eaapps.schoolsguide.domain.model.SocialModel
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(loginModel: LoginModel): Resource<DataAuth> {
        if (isValid(loginModel))
            return authRepository.login(LoginEntity(loginModel.email, loginModel.password))
        else
            throw IllegalArgumentException("Input is inValid")

    }

     fun isValid(loginModel: LoginModel): Boolean =
        loginModel.email.isNotBlank() && loginModel.password.isNotBlank()
}

class LoginBySocialUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(socialModel: SocialModel): Resource<DataAuth> {
        if (isValid(socialModel)) {
            return authRepository.loginBySocial(
                socialModel.provider,
                socialModel.social_id,
                socialModel.email,
                socialModel.fullName
            )
        } else
            throw IllegalArgumentException("Input is inValid")

    }

    private fun isValid(socialModel: SocialModel): Boolean =
        socialModel.provider.isNotBlank() && socialModel.social_id.isNotBlank() && socialModel.email.isNotBlank() && socialModel.fullName.isNotBlank()
}

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(registerModel: RegisterModel): Resource<DataAuth> {
        if (isValid(registerModel)) {
            return authRepository.register(
                registerModel.fullName, registerModel.email, registerModel.phone,
                registerModel.city, registerModel.district, registerModel.password
            )
        } else
            throw IllegalArgumentException("Input is inValid")

    }

    private fun isValid(registerModel: RegisterModel): Boolean =
        registerModel.fullName.isNotBlank() && registerModel.email.isNotBlank() && registerModel.phone.isNotBlank()
                && registerModel.password.isNotBlank() && registerModel.city.isNotBlank() && registerModel.district.isNotBlank()


}
