package com.eaapps.schoolsguide.utils

import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun <T : Any> safeCall(
    call: suspend () -> Resource<T>,
    errorMessage: String
): Resource<T> {
    return try {
        call()
    } catch (e: Exception) {
        val msg = when (e) {
            is UnknownHostException -> "No internet!"
            is ConnectException -> "No internet!"
            else -> e.message
        }
        e.printStackTrace()
        Resource.Exception(IOException(msg, e))
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