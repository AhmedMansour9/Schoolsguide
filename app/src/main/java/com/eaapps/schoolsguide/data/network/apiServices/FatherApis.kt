package com.eaapps.schoolsguide.data.network.apiServices

import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.ReviewRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FatherApis {

    @POST("/api/father/toggle_favorite")
    suspend fun toggleFavoriteAsync(@Query("school_id") school_id: Int): ResponseEntity

    @POST("/api/father/toggle_follow")
    suspend fun toggleFollowAsync(@Query("school_id") school_id: Int): ResponseEntity

    @POST("/api/father/toggle_recommend")
    suspend fun toggleRecommendedAsync(@Query("school_id") school_id: Int): ResponseEntity

    @POST("/api/father/setReview")
    suspend fun putReviewAsync(@Body reviewRequestEntity: ReviewRequestEntity): ResponseEntity

    @GET("/api/father/favoirtes")
    suspend fun loadFavoriteAsync(
        @Query("page") page: Int,
        @Query("per_page") limitedItemLoad: Int
    ): SchoolResponse

    @GET("api/father/profile")
    suspend fun loadProfileFatherAsync(): AuthResponse

}