package com.eaapps.schoolsguide.utils

import java.io.IOException

suspend fun <T : Any> safeCall(
    call: suspend () -> Resource<T>,
    errorMessage: String
): Resource<T> {
    return try {
        call()
    } catch (e: Exception) {
        Resource.Exception(IOException(errorMessage, e))
    }
}