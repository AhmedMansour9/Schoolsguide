package com.eaapps.schoolsguide.data.entity

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
