package com.eaapps.schoolsguide.domain.usecase

import android.content.res.Resources
import android.util.Patterns.EMAIL_ADDRESS
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ChangeFatherProfileEntity
import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.LoginModel
import com.eaapps.schoolsguide.domain.model.RegisterModel
import com.eaapps.schoolsguide.domain.model.SocialModel
import com.eaapps.schoolsguide.domain.model.UpdateProfileModel
import com.eaapps.schoolsguide.domain.repository.AuthRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository

) {

    suspend fun execute(loginModel: LoginModel): Resource<AuthResponse.AuthData> =
        authRepository.login(LoginEntity(loginModel.email, loginModel.password))

    fun isValid(loginModel: LoginModel): Boolean =
        (loginModel.email.isNotBlank() && EMAIL_ADDRESS.matcher(loginModel.email)
            .matches()) && (loginModel.password.isNotBlank() && loginModel.password.length >= 8)

    fun validMessage(resources: Resources, loginModel: LoginModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            loginModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }
            !EMAIL_ADDRESS.matcher(loginModel.email).matches() -> {
                return errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }

            loginModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.please_enter_pass))
                }
            }

            loginModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.password_must_8_char))
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

    fun validMessage(resources: Resources,registerModel: RegisterModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            registerModel.fullName.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", resources.getString(R.string.please_enter_full_name))
                }
            }

            registerModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }

            !EMAIL_ADDRESS.matcher(registerModel.email).matches() -> {
                return errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }

            registerModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", resources.getString(R.string.please_enter_phone))
                }
            }

            registerModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.please_enter_password))
                }
            }

            registerModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.password_must_8_char))
                }
            }

            registerModel.password != registerModel.confirmPassword -> {
                return errorMap.apply {
                    put("confirmPassword", resources.getString(R.string.confirm_password_must_match))
                }
            }

            registerModel.city < 0 -> {
                return errorMap.apply {
                    put("city", resources.getString(R.string.please_select_item_list))
                }
            }

            registerModel.district.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", resources.getString(R.string.please_enter_district))
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

    fun validMessage(resources: Resources,updateProfileModel: UpdateProfileModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            updateProfileModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", resources.getString(R.string.enter_full_name))
                }
            }

            updateProfileModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }

            !EMAIL_ADDRESS.matcher(updateProfileModel.email).matches() -> {
                return errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }

            updateProfileModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", resources.getString(R.string.please_enter_phone))
                }
            }

            updateProfileModel.city_id < 0 -> {
                return errorMap.apply {
                    put("city", resources.getString(R.string.please_select_item_list))
                }
            }

            else -> return errorMap

        }

    }


}