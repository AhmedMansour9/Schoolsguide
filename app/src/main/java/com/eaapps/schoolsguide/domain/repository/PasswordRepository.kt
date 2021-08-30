package com.eaapps.schoolsguide.domain.repository

import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResetPasswordRequestEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.utils.Resource

interface PasswordRepository {

    suspend fun resetPassword(resetPasswordRequestEntity: ResetPasswordRequestEntity): Resource<AuthResetResponse>

    suspend fun createPassword(email: String): Resource<AuthResetResponse>

    suspend fun updatePassword(changePasswordEntity: ChangePasswordEntity): Resource<ResponseEntity>

}