package com.eaapps.schoolsguide.features

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ActivityMainBinding
import com.eaapps.schoolsguide.utils.LanguageContextWrapper
import com.eaapps.schoolsguide.utils.fullScreenEnable
import com.eaapps.schoolsguide.utils.noInternetDialog
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var destinationChangedListener: NavController.OnDestinationChangedListener? = null

    lateinit var connectivityManager: ConnectivityManager

    private lateinit var dialogNoInternet: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogNoInternet = noInternetDialog {

        }

        navController = findNavController(R.id.nav_host_fragment)
        destinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment, R.id.loginFragment, R.id.registerFragment, R.id.forgetPasswordFragment, R.id.noInternetFragment ->
                        binding.bottomNav.visibleOrGone(false)
                    else -> binding.bottomNav.visibleOrGone(true)
                }

                if (destination.id == R.id.splashFragment)
                    fullScreenEnable(true, binding.root)
                else {
                    fullScreenEnable(false, binding.root)
                }
                binding.bottomNav.setOnApplyWindowInsetsListener(null)
            }
        binding.bottomNav.setupWithNavController(navController)


    }

    override fun onResume() {
        super.onResume()
        destinationChangedListener?.apply {
            navController.addOnDestinationChangedListener(this)
        }
    }

    override fun onPause() {
        super.onPause()
        destinationChangedListener?.apply {
            navController.removeOnDestinationChangedListener(this)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext: ContextWrapper = LanguageContextWrapper(newBase!!)
        super.attachBaseContext(localeUpdatedContext)
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connected()
        }

        override fun onLost(network: Network) {
            disconnected()
        }
    }

    private fun disconnected() {
        runOnUiThread {
            dialogNoInternet.show()
        }

    }

    private fun connected() {

    }

    override fun onStart() {
        super.onStart()
        val builder = NetworkRequest.Builder()
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        val networkRequest = builder.build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

private const val TAG = "MainActivity"