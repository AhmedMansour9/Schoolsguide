package com.eaapps.schoolsguide.data.network.apiServices

import com.eaapps.schoolsguide.data.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AuthenticationApis {

    @POST("/api/login")
    suspend fun loginAsync(@Body loginEntity: LoginEntity): AuthResponse

    @POST("/api/signupFather")
    suspend fun registerAsync(@Body registerEntity: RegisterEntity): AuthResponse

    @POST("/api/social_login")
    suspend fun loginBySocialAsync(@Body socialEntity: SocialEntity): AuthResponse

    @GET("/api/father/logout")
    suspend fun logoutAsync(): ResponseEntity

    @Multipart
    @POST("/api/father/editFatherProfile")
    suspend fun changeFatherProfileAsync(
        @Part("_method") _method: RequestBody,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("gender") gender: RequestBody? = null,
        @Part image: MultipartBody.Part? = null,
    ): ResponseEntity

}