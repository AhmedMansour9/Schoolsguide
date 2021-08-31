package com.eaapps.schoolsguide.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import java.io.IOException

object NetworkChecker {

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.let { it ->
            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nc =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                nc?.let {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )
                }
            } else {
                connectivityManager.activeNetworkInfo?.let {
                    return it.isConnected && (it.type == ConnectivityManager.TYPE_WIFI || it.type == ConnectivityManager.TYPE_MOBILE)
                }
            }) ?: false
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun isOnline(): Flow<Resource<Boolean>> {
        return callbackFlow {
            //if (isNetworkConnected(context)) {
                OkHttpClient().newCall(
                    Request.Builder()
                        .url("https://www.google.com")
                        .build()
                ).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        trySend(Resource.Error<Boolean>(0, e.message!!))

                    }

                    override fun onResponse(call: Call, response: Response) {
                        trySend(Resource.Success(response.code == 200))
                    }

                })
//            }else{
//
//            }
            awaitClose()
        }
    }

}