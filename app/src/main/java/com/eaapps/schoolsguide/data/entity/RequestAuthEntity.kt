package com.eaapps.schoolsguide.data.entity

data class LoginEntity(var email: String, var password: String)

data class RegisterEntity(
    var fullName: String,
    var email: String,
    var phone: String,
    var city: String,
    var district: String,
    var password: String,
)

data class SocialEntity(
    var provider: String,
    var social_id: String,
    var email: String,
    var fullName: String
)
