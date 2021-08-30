package com.eaapps.schoolsguide.domain.usecase

import android.util.Patterns
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

    fun validMessage(email: String): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        return when {
            email.isBlank() -> {
                HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorMap.apply {
                    put("email", "Please Enter Validation Email")
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

    fun validMessage(resetPasswordModel: ResetPasswordModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            resetPasswordModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(resetPasswordModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            resetPasswordModel.password.isBlank() -> {
                return errorMap.apply {
                    put("password", "Please Enter Password")
                }
            }

            resetPasswordModel.password.length < 8 -> {
                return errorMap.apply {
                    put("password", "Password must be at least 8 characters")
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

    fun validMessage(updatePasswordModel: UpdatePasswordModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {

            updatePasswordModel.oldPassword.isBlank() -> {
                return errorMap.apply {
                    put("oldPassword", "For your security,please enter old password")
                }
            }

            updatePasswordModel.password.isBlank() -> {
                return errorMap.apply {
                    put("newPassword", "Please Enter New Password")
                }
            }

            updatePasswordModel.password.length < 8 -> {
                return errorMap.apply {
                    put("newPassword", "Password must be at least 8 characters")
                }
            }

            updatePasswordModel.password != updatePasswordModel.confirmPassword -> {
                return errorMap.apply {
                    put("confirmPassword", "confirm password must match the password")
                }
            }

            else -> return errorMap

        }

    }

}