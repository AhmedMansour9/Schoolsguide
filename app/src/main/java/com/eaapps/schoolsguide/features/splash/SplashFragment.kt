package com.eaapps.schoolsguide.features.splash

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentSplashBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)
    private var countDownTimer: CountDownTimer? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindProfileCollectResult()
        binding.bindLogoutCollectResult()
    }

    private fun FragmentSplashBinding.bindProfileCollectResult() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(onError = {
                progressCircle.visibleOrGone(false)
                setupCountDown(false)
            }, onSuccess = {
                if (it.email.isNotBlank())
                    setupCountDown(true)
                else
                    setupCountDown(false)
            }, onException = {
                progressCircle.visibleOrGone(false)
                navigationToNoInternet()
            }, onLoading = {
                progressCircle.visibleOrGone(true)

            }
            ))
        }

    }

    private fun FragmentSplashBinding.bindLogoutCollectResult() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.logoutStateFlow.stateFlow.collect(FlowEvent(onError = {
                setupCountDown(true)
            }, onSuccess = {
                setupCountDown(false)
            }, onLoading = {
                progressCircle.visibleOrGone(true)
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

    private fun navigationToNoInternet() =
        launchFragment(SplashFragmentDirections.actionSplashFragmentToNoInternetFragment())

    override fun onStart() {
        super.onStart()
        countDownTimer?.start()
    }

    override fun onStop() {
        countDownTimer?.cancel()
        super.onStop()
    }
}