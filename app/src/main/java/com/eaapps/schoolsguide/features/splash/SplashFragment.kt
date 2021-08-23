package com.eaapps.schoolsguide.features.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.launchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var countDownTimer: CountDownTimer? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(onError = {
                setupCountDown(false)
            }, onSuccess = {
                if (it.email.isNotBlank())
                    setupCountDown(true)
                else
                    setupCountDown(false)
            }
            ))
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.logoutStateFlow.stateFlow.collect(FlowEvent(onError = {
                setupCountDown(true)
            }, onSuccess = {
                setupCountDown(false)
            }
            ))
        }
    }

    private fun setupCountDown(home: Boolean) {
        countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (home)
                    navigationToHome()
                else
                    navigationToLogin()

            }
        }.start()
    }

    private fun navigationToLogin() =
        launchFragment(SplashFragmentDirections.actionSplashFragmentToLoginFragment())

    private fun navigationToHome() =
        launchFragment(SplashFragmentDirections.actionSplashFragmentToHomeFragment())


    override fun onStart() {
        super.onStart()
        countDownTimer?.start()
    }

    override fun onStop() {
        countDownTimer?.cancel()
        super.onStop()
    }
}