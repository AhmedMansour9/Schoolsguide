package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    @POST("/api/login")
    fun loginAsync(@Body loginEntity: LoginEntity): Deferred<Response<AuthResponse>>

    @POST("/api/signupFather")
    fun registerAsync(@Body registerEntity: RegisterEntity): Deferred<Response<AuthResponse>>

    @POST("/api/social_login")
    fun loginBySocialAsync(@Body socialEntity: SocialEntity): Deferred<Response<AuthResponse>>

    @GET("api/father/profile")
    fun loadProfileFatherAsync(): Deferred<Response<AuthResponse>>

    @POST("/api/changePassword")
    fun changePasswordAsync(@Body changePasswordEntity: ChangePasswordEntity): Deferred<Response<ResponseEntity>>

    @POST("/api/father/editFatherProfile")
    fun changeFatherProfileAsync(
        @Body changeFatherProfileEntity: ChangeFatherProfileEntity
    ): Deferred<Response<ResponseEntity>>

    @POST("/api/add_school_request")
    fun addSchoolAsync(@Body addSchoolEntity: AddSchoolEntity): Deferred<Response<ResponseEntity>>

    @GET("/api/cities")
    fun getCitiesAsync(): Deferred<Response<CityResponse>>

    @GET("/api/types")
    fun getSchoolTypeAsync(): Deferred<Response<TypeResponse>>

    @GET("/api/sliders")
    fun getSliderAsync(): Deferred<Response<SliderResponse>>

    @GET("/api/schools?page=1&perPage=8&recommended")
    fun getRecommendedSchoolAsync(): Deferred<Response<SchoolResponse>>

    @GET("/api/schools?page=1&perPage=8&featured")
    fun getFeaturedSchoolAsync(): Deferred<Response<SchoolResponse>>

    @POST("/api/father/toggle_favorite")
    fun toggleFavoriteAsync(@Query("school_id") school_id: Int): Deferred<Response<ResponseEntity>>

    @GET("/api/father/favoirtes")
    suspend fun loadFavoriteAsync(
        @Query("page") page: Int,
        @Query("per_page") limitedItemLoad: Int
    ): SchoolResponse

    @GET("/api/schools?recommended")
    suspend fun loadAllRecommendedSchool(
        @Query("page") page: Int,
        @Query("per_page") limitedItemLoad: Int
    ): SchoolResponse

    @GET("/api/schools?featured")
    suspend fun loadAllFeaturedSchool(
        @Query("page") page: Int,
        @Query("per_page") limitedItemLoad: Int
    ): SchoolResponse



    @GET("/api/schools?featured")
    suspend fun loadAllTypedSchool(
        @Query("type_id") type_id: Int,
        @Query("page") page: Int,
        @Query("per_page") limitedItemLoad: Int
    ): SchoolResponse





}