package com.eaapps.schoolsguide.utils

sealed class Resource<out T : Any> {
    data class Success<T : Any>(var data: T?) : Resource<T>()
    data class Loading<T : Any>(var data: T? = null) : Resource<T>()
    data class Errors<T : Any>(var errorEntity: ErrorEntity? = null) : Resource<T>()
    data class Error<T : Any>(val errorCode: Int, val errorMessage: String) : Resource<T>()
    data class Exception<T : Any>(var error: kotlin.Exception) : Resource<T>()
    class Nothing<T : Any> : Resource<T>()
}

sealed class ErrorEntity {
    object TimeOut : ErrorEntity()
    object NoInternet : ErrorEntity()
    object ServerNotResponse : ErrorEntity()
    object ParseError : ErrorEntity()
    data class HttpError(var httpError: HttpErrorEntity? = null) : ErrorEntity()
    data class IOError(var msg: String? = null) : ErrorEntity()
    object NothingError : ErrorEntity()
}

sealed class HttpErrorEntity {
    object BadRequest400 : HttpErrorEntity()
    object Unauthorized401 : HttpErrorEntity()
    object Forbidden403 : HttpErrorEntity()
    object NotFound404 : HttpErrorEntity()
    object NotAcceptable406 : HttpErrorEntity()
    object ServerError500 : HttpErrorEntity()
    object NotImplemented501 : HttpErrorEntity()
    object ServiceUnavailable503 : HttpErrorEntity()
    object TimeoutGateway504 : HttpErrorEntity()
    object BadGateway502 : HttpErrorEntity()
    data class Nothing(var msg:String?=null) : HttpErrorEntity()
}