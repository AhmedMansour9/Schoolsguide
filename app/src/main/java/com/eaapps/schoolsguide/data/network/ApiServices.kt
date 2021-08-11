package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServices {

    @POST("/api/login")
    fun loginAsync(@Body loginEntity: LoginEntity): Deferred<Response<AuthResponse>>

    @POST("/api/signupFather")
    fun registerAsync(@Body registerEntity: RegisterEntity): Deferred<Response<AuthResponse>>

    @POST("/api/social_login")
    fun loginBySocialAsync(@Body socialEntity: SocialEntity): Deferred<Response<AuthResponse>>

    @GET("api/father/profile")
    fun loadProfileFatherAsync(@Header("Authorization") token: String): Deferred<Response<AuthResponse>>

    @POST("/api/changePassword")
    fun changePasswordAsync(@Body changePasswordEntity: ChangePasswordEntity): Deferred<Response<ResponseEntity>>

    @POST("/api/editFatherProfile")
    fun changeFatherProfileAsync(@Body changeFatherProfileEntity: ChangeFatherProfileEntity): Deferred<Response<ResponseEntity>>

    @POST("/api/add_school")
    fun addSchoolAsync(@Body addSchoolEntity: AddSchoolEntity): Deferred<Response<ResponseEntity>>

    @GET("/api/cities")
    fun getCitiesAsync(): Deferred<Response<CityResponse>>

    @GET("/api/types")
    fun getSchoolTypeAsync(): Deferred<Response<TypeResponse>>

    @GET("/api/sliders")
    fun getSliderAsync(): Deferred<Response<SliderResponse>>

    @GET("api/schools?page=1&perPage=8&recommended")
    fun getRecommendedSchoolAsync(): Deferred<Response<SchoolResponse>>

    @GET("api/schools?page=1&perPage=8&featured")
    fun getFeaturedSchoolAsync(): Deferred<Response<SchoolResponse>>


}