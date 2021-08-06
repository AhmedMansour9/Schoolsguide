package com.eaapps.schoolsguide.data.entity

import com.google.gson.annotations.SerializedName

data class DataAuth(
    @SerializedName("full_name") val full_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("city") val city: City,
    @SerializedName("district") val district: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("expires_at") val expires_at: String
)

data class ResponseAuth(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DataAuth? = null
)

data class ResponseError(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)