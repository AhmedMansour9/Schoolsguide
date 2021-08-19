package com.eaapps.schoolsguide.data.entity

import okhttp3.MultipartBody

data class LoginEntity(var email: String, var password: String)

data class RegisterEntity(
    var full_name: String,
    var email: String,
    var city_id: String,
    var password: String,
    var password_confirmation: String,
    var phone: String,
    var district: String,
)

data class SocialEntity(
    var provider: String,
    var social_id: String,
    var email: String,
    var fullName: String
)

data class AddSchoolEntity(
    var school_name: String,
    var phone: String,
    var email: String,
    var notes: String
)

data class ChangePasswordEntity(
    var old_password: String,
    var password: String,
    var password_confirmation: String
)

data class ChangeFatherProfileEntity(
    var full_name: String,
    var email: String,
    var phone: String,
    var city_id: Int,
    var gender: String? = null,
    var image: MultipartBody.Part? = null,
    val _method: String = "put"
)

data class FilterRequestEntity(
    var search: String? = null,
    var school_type: String? = null,
    var type_id: Int? = null,
    var grade_id: Int? = null,
    var from_price: Int? = null,
    var to_price: Int? = null,
    var program_id: Int? = null,
    var city_id: Int? = null,
    var review: Int? = null
)

data class ResetPasswordRequestEntity(
    var email: String,
    var password: String,
    var password_confirmation: String,
    var token: String
)

data class InquiryRequestEntity(
    var message_type: String,
    var reply_type: String,
    var prefered_reply_time: String,
    var message: String,
    var school_id: Int
)

data class DiscountRequestEntity(
    var full_name: String = "",
    var phone: String = "",
    var email: String = "",
    var number_of_students: Int? = null,
    var school_id: Int? = null
)