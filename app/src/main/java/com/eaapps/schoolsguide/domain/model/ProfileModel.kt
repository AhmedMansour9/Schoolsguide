package com.eaapps.schoolsguide.domain.model

import okhttp3.MultipartBody

data class AddSchoolModel(
    var school_name: String = "",
    var phone: String = "",
    var email: String = "",
    var notes: String = ""
)

data class UpdatePasswordModel(
    var oldPassword: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

data class UpdateProfileModel(
    var full_name: String = "",
    var email: String = "",
    var phone: String = "",
    var city_id: Int = 1,
    var gender: String = "",
    var image: MultipartBody.Part? = null
)

data class ReviewModel(
    var school_id: Int = -1,
    var comment: String = "",
    var education: Int = 0,
    var communication: Int = 0,
    var facilities: Int = 0,
    var safety: Int = 0,
    var activities: Int = 0
)