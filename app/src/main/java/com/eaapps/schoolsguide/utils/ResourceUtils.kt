package com.eaapps.schoolsguide.utils

import com.eaapps.schoolsguide.utils.Resource.Errors
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T : Any> safeCall(
    call: suspend () -> Resource<T>,
    errorMessage: String
): Resource<T> {
    return try {
        call()
    } catch (e: Exception) {
        return when (e) {
            is SocketTimeoutException -> Errors(ErrorEntity.TimeOut)
            is UnknownHostException -> Errors(ErrorEntity.NoInternet)
            is ConnectException -> Errors(ErrorEntity.ServerNotResponse)
            is IOException -> Errors(ErrorEntity.IOError(e.message))
            is HttpException -> Errors(
                errorEntity = ErrorEntity.HttpError(
                    when (e.code()) {
                        400 -> HttpErrorEntity.BadRequest400
                        401 -> HttpErrorEntity.Unauthorized401
                        403 -> HttpErrorEntity.Forbidden403
                        404 -> HttpErrorEntity.NotFound404
                        406 -> HttpErrorEntity.NotAcceptable406
                        500 -> HttpErrorEntity.ServerError500
                        501 -> HttpErrorEntity.NotImplemented501
                        503 -> HttpErrorEntity.ServiceUnavailable503
                        504 -> HttpErrorEntity.TimeoutGateway504
                        502 -> HttpErrorEntity.BadGateway502
                        else -> HttpErrorEntity.Nothing(e.message())
                    }
                )
            )
            is JSONException, is JsonSyntaxException -> Errors(ErrorEntity.ParseError)
            else -> Errors(ErrorEntity.NothingError)
        }
    }
}

fun <T : Any> resourceError(code: Int): Resource.Error<T> {
    val msg: String = when (code) {
        400 -> "Server is under maintenance"
        401 -> "You are not authorized to perform this operation"
        403 -> "The client does not have access rights to the content"
        404 -> "he server can not find the requested resource. In the browser, this means the URL is not recognized"
        else -> "try later"
    }


    return Resource.Error<T>(code, msg)
}