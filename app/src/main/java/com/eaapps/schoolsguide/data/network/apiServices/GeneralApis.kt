package com.eaapps.schoolsguide.data.network.apiServices

import com.eaapps.schoolsguide.data.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface GeneralApis {
    @GET("/api/cities")
    suspend fun getCitiesAsync(): CityResponse

    @POST("/api/add_school_request")
    suspend fun addSchoolAsync(@Body addSchoolEntity: AddSchoolEntity): ResponseEntity

    @GET("/api/types")
    suspend fun getSchoolTypeAsync(): TypeResponse

    @GET("/api/grades")
    suspend fun getSchoolGradesAsync(): GradesResponse

    @GET("/api/programs")
    suspend fun getSchoolProgramsAsync(): ProgramsResponse

    @GET("/api/sliders")
    suspend fun getSliderAsync(): SliderResponse

    @GET("/api/schools?page=1&perPage=8&recommended")
    suspend fun getRecommendedSchoolAsync(): SchoolResponse

    @GET("/api/schools?page=1&perPage=8&featured")
    suspend fun getFeaturedSchoolAsync(): SchoolResponse

    @POST("/api/add_enquery")
    suspend fun addInquiryAsync(@Body addInquiryRequestEntity: InquiryRequestEntity): ResponseEntity

    @POST("/api/bookSchool")
    suspend fun bookSchoolAsync(@Body body: HashMap<String, Any>): ResponseEntity

    @Multipart
    @POST("/api/upload_cv")
    suspend fun uploadCvAsync(
        @Part("job_id") job_id: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part attachment: MultipartBody.Part
    ): ResponseEntity

    @POST("/api/joinDiscount")
    suspend fun joinDiscountAsync(@Body discountRequestEntity: DiscountRequestEntity): ResponseEntity

    @GET("/api/schoolDetails/")
    suspend fun schoolDetailsAsync(@Query("school_id") school_id: Int): SchoolDetailsResponse

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
    ): SchoolResponse


    @GET("/api/schools")
    suspend fun filterMapSchoolAsync(
        @Query("search") search: String? = null,
        @Query("type_id") type_id: Int? = null,
        @Query("school_type") school_type: String? = null,
        @Query("grade_id") grade_id: Int? = null,
        @Query("from_price") from_price: Int? = null,
        @Query("to_price") to_price: Int? = null,
        @Query("program_id") program_id: Int? = null,
        @Query("city_id") city_id: Int? = null
    ): SchoolResponse

}