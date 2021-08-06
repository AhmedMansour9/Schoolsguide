package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {

    @POST("/api/login")
    fun loginAsync(@Body loginEntity: LoginEntity): Deferred<Response<ResponseAuth>>

    @POST("/api/signupFather")
    fun registerAsync(@Body registerEntity: RegisterEntity): Deferred<Response<ResponseAuth>>


    @POST("/api/social_login")
    fun loginBySocialAsync(@Body socialEntity: SocialEntity): Deferred<Response<ResponseAuth>>

    @GET("/api/cities")
    fun getCitiesAsync() : Deferred<Response<CityResponse>>



}