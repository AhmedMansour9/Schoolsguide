package com.eaapps.schoolsguide.utils

import com.eaapps.schoolsguide.utils.Resource.Errors
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

suspend fun <T : Any> safeCall(
    call: suspend () -> Resource<T>,
    errorMessage: String
): Resource<T> {
    return try {
        call()
    } catch (e: Exception) {
        return Errors(filterError(e))
    }
}

fun <T : Throwable> filterError(exception: T): ErrorEntity {
    return when (exception) {
        is SocketTimeoutException -> ErrorEntity.TimeOut
        is UnknownHostException -> ErrorEntity.NoInternet
        is ConnectException -> ErrorEntity.ServerNotResponse
        is IOException -> ErrorEntity.IOError(exception.message)
        is HttpException -> ErrorEntity.HttpError(
            when (exception.code()) {
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
                else -> HttpErrorEntity.Nothing(exception.message())
            }
        )

        is SSLException -> ErrorEntity.SSLError
        is JSONException, is JsonSyntaxException -> ErrorEntity.ParseError
        else -> ErrorEntity.NothingError
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