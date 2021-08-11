package com.eaapps.schoolsguide.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ActivityMainBinding
import com.eaapps.schoolsguide.utils.fullScreenEnable
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var destinationChangedListener: NavController.OnDestinationChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)
        destinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment, R.id.loginFragment, R.id.registerFragment ->
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
}