package com.eaapps.schoolsguide.data.network.repositories

import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResetPasswordRequestEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.network.apiServices.PasswordApis
import com.eaapps.schoolsguide.domain.repository.PasswordRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PasswordRepositoryImpl @Inject constructor(private val passwordApis: PasswordApis) :
    PasswordRepository {

    override suspend fun resetPassword(resetPasswordRequestEntity: ResetPasswordRequestEntity): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = passwordApis.resetPasswordAsync(resetPasswordRequestEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun createPassword(email: String): Resource<AuthResetResponse> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val hashCreatePassword = HashMap<String, String>().apply {
                    put("email", email)
                }
                val result = passwordApis.createPasswordAsync(hashCreatePassword)
                Resource.Success(result)
            }, "Exception occurred!")
        }


    override suspend fun updatePassword(changePasswordEntity: ChangePasswordEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = passwordApis.changePasswordAsync(changePasswordEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }
}