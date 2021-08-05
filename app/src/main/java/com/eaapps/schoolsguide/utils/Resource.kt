package com.eaapps.schoolsguide.utils

sealed class Resource<out T : Any> {
    data class Success<T : Any>(var data: T?) : Resource<T>()
    data class Loading<T : Any>(var data: T? = null) : Resource<T>()
    data class Error<T : Any>(val errorCode: Int, val errorMessage: String) : Resource<T>()
    data class Exception<T : Any>(var error: kotlin.Exception) : Resource<T>()
    class Nothing<T : Any> : Resource<T>()
}
