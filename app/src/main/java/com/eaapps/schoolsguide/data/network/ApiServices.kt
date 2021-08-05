package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.LoginEntity
import com.eaapps.schoolsguide.data.entity.RegisterEntity
import com.eaapps.schoolsguide.data.entity.ResponseAuth
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServices {

    @POST("/api/login")
    fun login(@Body loginEntity: LoginEntity): Deferred<Response<ResponseAuth>>

    @POST("/api/signupFather")
    fun register(@Body registerEntity: RegisterEntity): Deferred<Response<ResponseAuth>>

}