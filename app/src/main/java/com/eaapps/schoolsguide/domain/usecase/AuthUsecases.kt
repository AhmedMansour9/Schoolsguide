package com.eaapps.schoolsguide.domain.usecase

import android.util.Patterns.EMAIL_ADDRESS
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.*
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(loginModel: LoginModel): Resource<AuthResponse.AuthData> =
        authRepository.login(LoginEntity(loginModel.email, loginModel.password))

    fun isValid(loginModel: LoginModel): Boolean =
        (loginModel.email.isNotBlank() && EMAIL_ADDRESS.matcher(loginModel.email)
            .matches()) && (loginModel.password.isNotBlank() && loginModel.password.length >= 8)

    fun validMessage(loginModel: LoginModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            loginModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }
            !EMAIL_ADDRESS.matcher(loginModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            loginModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", "Please Enter Password")
                }
            }

            loginModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", "Password must be at least 8 characters")
                }
            }

            else -> return errorMap

        }

    }

}

class LoginBySocialUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(socialModel: SocialModel): Resource<AuthResponse.AuthData> {
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

    suspend fun execute(registerModel: RegisterModel): Resource<AuthResponse.AuthData> =
        authRepository.register(
            registerModel.fullName,
            registerModel.email,
            registerModel.phone,
            registerModel.city,
            registerModel.district,
            registerModel.password,
            registerModel.confirmPassword
        )

    fun isValid(registerModel: RegisterModel): Boolean =
        registerModel.fullName.isNotBlank()
                && registerModel.email.isNotBlank()
                && EMAIL_ADDRESS.matcher(registerModel.email).matches()
                && registerModel.phone.isNotBlank()
                && registerModel.password.isNotBlank()
                && registerModel.password.length > 8
                && registerModel.confirmPassword.isNotBlank()
                && registerModel.password == registerModel.confirmPassword
                && registerModel.city > -1
                && registerModel.district.isNotBlank()

    fun validMessage(registerModel: RegisterModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            registerModel.fullName.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", "Please Enter Full Name")
                }
            }

            registerModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }

            !EMAIL_ADDRESS.matcher(registerModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            registerModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", "Please Enter Phone Number")
                }
            }


            registerModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", "Please Enter Password")
                }
            }

            registerModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", "Password must be at least 8 characters")
                }
            }

            registerModel.password != registerModel.confirmPassword -> {
                return errorMap.apply {
                    put("confirmPassword", "confirm password must match the password")
                }
            }

            registerModel.city < 0 -> {
                return errorMap.apply {
                    put("city", "Please select an item in the list")
                }
            }

            registerModel.district.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", "Please Enter District site")
                }
            }

            else -> return errorMap

        }

    }


}

class LogoutFatherUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun execute(): Resource<ResponseEntity> =
        authRepository.logoutFather()
}

class UpdateFatherProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun execute(
        updateProfileModel: UpdateProfileModel
    ): Resource<ResponseEntity> =
        authRepository.updateProfileFather(
            ChangeFatherProfileEntity(
                updateProfileModel.full_name,
                updateProfileModel.email,
                updateProfileModel.phone,
                updateProfileModel.city_id,
                updateProfileModel.gender,
                updateProfileModel.image
            )
        )

    fun isValid(updateProfileModel: UpdateProfileModel): Boolean =
        updateProfileModel.full_name.isNotBlank()
                && updateProfileModel.email.isNotBlank()
                && EMAIL_ADDRESS.matcher(updateProfileModel.email).matches()
                && updateProfileModel.phone.isNotBlank()
                && updateProfileModel.city_id > -1

    fun validMessage(updateProfileModel: UpdateProfileModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            updateProfileModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", "Please Enter Full Name")
                }
            }

            updateProfileModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }

            !EMAIL_ADDRESS.matcher(updateProfileModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            updateProfileModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", "Please Enter Phone Number")
                }
            }

            updateProfileModel.city_id < 0 -> {
                return errorMap.apply {
                    put("city", "Please select an item in the list")
                }
            }

            else -> return errorMap

        }

    }


}