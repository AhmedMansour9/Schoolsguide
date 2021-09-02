package com.eaapps.schoolsguide.domain.usecase

import android.content.res.Resources
import android.util.Patterns
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResetPasswordRequestEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.ResetPasswordModel
import com.eaapps.schoolsguide.domain.model.UpdatePasswordModel
import com.eaapps.schoolsguide.domain.repository.PasswordRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject

class CreateNewPasswordUseCase @Inject constructor(private val passwordRepository: PasswordRepository) {
    suspend fun execute(email: String): Resource<AuthResetResponse> =
        passwordRepository.createPassword(email)

    fun isValid(email: String): Boolean =
        (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches())

    fun validMessage(resources: Resources,email: String): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        return when {
            email.isBlank() -> {
                HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_phone))
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }
            else -> errorMap
        }

    }
}

class ResetNewPasswordUseCase @Inject constructor(private val passwordRepository: PasswordRepository) {
    suspend fun execute(resetPasswordModel: ResetPasswordModel): Resource<AuthResetResponse> =
        passwordRepository.resetPassword(
            ResetPasswordRequestEntity(
                resetPasswordModel.email,
                resetPasswordModel.password,
                resetPasswordModel.password,
                resetPasswordModel.token
            )
        )


    fun isValid(resetPasswordModel: ResetPasswordModel): Boolean =
        (resetPasswordModel.email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(resetPasswordModel.email)
            .matches()) && (resetPasswordModel.password.isNotBlank() && resetPasswordModel.password.length >= 8)

    fun validMessage(resources: Resources,resetPasswordModel: ResetPasswordModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            resetPasswordModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email",resources.getString(R.string.please_enter_email))
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(resetPasswordModel.email).matches() -> {
                return errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }

            resetPasswordModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.please_enter_password))
                }
            }

            resetPasswordModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", resources.getString(R.string.password_must_8_char))
                }
            }

            else -> return errorMap

        }

    }
}

class UpdatePasswordUseCase @Inject constructor(private val passwordRepository: PasswordRepository) {

    suspend fun execute(updatePasswordModel: UpdatePasswordModel): Resource<ResponseEntity> =
        passwordRepository.updatePassword(
            ChangePasswordEntity(
                updatePasswordModel.oldPassword,
                updatePasswordModel.password,
                updatePasswordModel.password
            )
        )

    fun isValid(updatePasswordModel: UpdatePasswordModel): Boolean =
        updatePasswordModel.oldPassword.isNotBlank()
                && (updatePasswordModel.password.isNotBlank()) && updatePasswordModel.confirmPassword.isNotBlank()
                && updatePasswordModel.password.length > 8
                && updatePasswordModel.password == updatePasswordModel.confirmPassword

    fun validMessage(resources: Resources,updatePasswordModel: UpdatePasswordModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {

            updatePasswordModel.oldPassword.isBlank() -> {
                return errorMap.apply {
                    put("oldPassword", resources.getString(R.string.for_your_security))
                }
            }

            updatePasswordModel.password.isBlank() -> {
                return errorMap.apply {
                    put("newPassword", resources.getString(R.string.please_enter_new_password))
                }
            }

            updatePasswordModel.password.length < 8 -> {
                return errorMap.apply {
                    put("newPassword", resources.getString(R.string.password_must_8_char))
                }
            }

            updatePasswordModel.password != updatePasswordModel.confirmPassword -> {
                return errorMap.apply {
                    put("confirmPassword",resources.getString(R.string.confirm_password_must_match))
                }
            }

            else -> return errorMap

        }

    }

}