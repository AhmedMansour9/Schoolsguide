package com.eaapps.schoolsguide.features.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.eaapps.schoolsguide.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCountDown()
    }

    private fun setupCountDown() {
        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                navigationToLogin()
            }
        }
    }

    private fun navigationToLogin() =
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())

    override fun onStart() {
        super.onStart()
        countDownTimer.start()
    }

    override fun onStop() {
        countDownTimer.cancel()
        super.onStop()
    }
}