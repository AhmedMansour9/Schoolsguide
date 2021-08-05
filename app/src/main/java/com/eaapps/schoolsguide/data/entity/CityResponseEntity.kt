package com.eaapps.schoolsguide.data.entity

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<City>,
    @SerializedName("message") val message: String
)

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

