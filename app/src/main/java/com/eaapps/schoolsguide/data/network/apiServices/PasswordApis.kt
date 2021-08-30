package com.eaapps.schoolsguide.data.network.apiServices

import com.eaapps.schoolsguide.data.entity.AuthResetResponse
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResetPasswordRequestEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface PasswordApis {

    @POST("/api/changePassword")
    suspend fun changePasswordAsync(@Body changePasswordEntity: ChangePasswordEntity): ResponseEntity

    @POST("/api/password/create")
    suspend fun createPasswordAsync(@Body body: HashMap<String, String>): AuthResetResponse

    @POST("/api/password/reset")
    suspend fun resetPasswordAsync(@Body resetPasswordRequestEntity: ResetPasswordRequestEntity): AuthResetResponse

}