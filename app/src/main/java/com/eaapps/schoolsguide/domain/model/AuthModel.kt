package com.eaapps.schoolsguide.domain.model

data class LoginModel(
    var email: String = "",
    var password: String = "",
    var remember: Boolean = false
)

data class RegisterModel(
    var fullName: String = "",
    var email: String = "",
    var phone: String = "",
    var city: Int = -1,
    var district: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

data class SocialModel(
    var provider: String = "",
    var social_id: String = "",
    var email: String="",
    var fullName: String=""
)


data class ResetPasswordModel(
    var email: String = "",
    var password: String = "",
    var password_confirmation: String = "",
    var token: String = ""
)