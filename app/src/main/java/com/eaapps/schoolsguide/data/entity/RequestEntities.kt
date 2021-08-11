package com.eaapps.schoolsguide.data.entity

import java.io.File

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
    var gender: String,
    var image: File
)