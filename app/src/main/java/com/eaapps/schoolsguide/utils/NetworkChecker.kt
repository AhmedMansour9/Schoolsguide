package com.eaapps.schoolsguide.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

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

}