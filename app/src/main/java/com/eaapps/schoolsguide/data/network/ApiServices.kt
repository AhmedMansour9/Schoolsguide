package com.eaapps.schoolsguide.data.network

import com.eaapps.schoolsguide.data.entity.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @POST("/api/password/create")
    fun createPasswordAsync(@Body body: HashMap<String, String>): Deferred<Response<AuthResetResponse>>

    @POST("/api/password/reset")
    fun resetPasswordAsync(@Body resetPasswordRequestEntity: ResetPasswordRequestEntity): Deferred<Response<AuthResetResponse>>

    @GET("/api/father/logout")
    fun logoutAsync(): Deferred<Response<ResponseEntity>>


    @Multipart
    @POST("/api/father/editFatherProfile")
    fun changeFatherProfileAsync(
        @Part("_method") _method: RequestBody,
        @Part("full_name") full_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("gender") gender: RequestBody? = null,
        @Part image: MultipartBody.Part,
        //@Body params: RequestBody
//           image: File?=File("/storage/emulated/0/Android/iPad – 1.png")
        //@Body changeFatherProfileEntity: ChangeFatherProfileEntity
    ): Deferred<Response<ResponseEntity>>

    @POST("/api/add_school_request")
    fun addSchoolAsync(@Body addSchoolEntity: AddSchoolEntity): Deferred<Response<ResponseEntity>>

    @GET("/api/cities")
    fun getCitiesAsync(): Deferred<Response<CityResponse>>

    @GET("/api/types")
    fun getSchoolTypeAsync(): Deferred<Response<TypeResponse>>

    @GET("/api/grades")
    fun getSchoolGradesAsync(): Deferred<Response<GradesResponse>>

    @GET("/api/programs")
    fun getSchoolProgramsAsync(): Deferred<Response<ProgramsResponse>>

    @GET("/api/sliders")
    fun getSliderAsync(): Deferred<Response<SliderResponse>>

    @GET("/api/schools?page=1&perPage=8&recommended")
    fun getRecommendedSchoolAsync(): Deferred<Response<SchoolResponse>>

    @GET("/api/schools?page=1&perPage=8&featured")
    fun getFeaturedSchoolAsync(): Deferred<Response<SchoolResponse>>

    @POST("/api/father/toggle_favorite")
    fun toggleFavoriteAsync(@Query("school_id") school_id: Int): Deferred<Response<ResponseEntity>>

    @POST("/api/father/toggle_follow")
    fun toggleFollowAsync(@Query("school_id") school_id: Int): Deferred<Response<ResponseEntity>>

    @POST("/api/father/toggle_recommend")
    fun toggleRecommendedAsync(@Query("school_id") school_id: Int): Deferred<Response<ResponseEntity>>

    @POST("/api/add_enquery")
    fun addInquiryAsync(@Body addInquiryRequestEntity: InquiryRequestEntity): Deferred<Response<ResponseEntity>>

    @POST("/api/bookSchool")
    fun bookSchoolAsync(@Body body: HashMap<String, Any>): Deferred<Response<ResponseEntity>>

    @POST("/api/joinDiscount")
    fun joinDiscountAsync(@Body discountRequestEntity:DiscountRequestEntity): Deferred<Response<ResponseEntity>>

    @GET("/api/schoolDetails/")
    fun schoolDetailsAsync(@Query("school_id") school_id: Int): Deferred<Response<SchoolDetailsResponse>>

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


    @GET("/api/schools")
    suspend fun filterSchool(
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("search") search: String? = null,
        @Query("type_id") type_id: Int? = null,
        @Query("school_type") school_type: String? = null,
        @Query("grade_id") grade_id: Int? = null,
        @Query("from_price") from_price: Int? = null,
        @Query("to_price") to_price: Int? = null,
        @Query("program_id") program_id: Int? = null,
        @Query("city_id") city_id: Int? = null
    ): Response<SchoolResponse>


}