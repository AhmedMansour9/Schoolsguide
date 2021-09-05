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
import com.eaapps.schoolsguide.utils.*
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
        checkInternet()
        binding.bindProfileCollectResult()
        binding.bindLogoutCollectResult()
    }

    private fun checkInternet() {
        lifecycleScope.launchWhenStarted {
            NetworkChecker.isOnline().collect(
                FlowEvent(
                    onError = {
                        requireActivity().noInternetDialog {
                            mainViewModel.loadProfile()
                        }
                    },
                    onSuccess = {
                        mainViewModel.loadProfile()
                    })
            )
        }
    }

    private fun FragmentSplashBinding.bindProfileCollectResult() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(
                onSuccess = {
                    progressCircle.visibleOrGone(false)
                    if (it.email.isNotBlank())
                        setupCountDown(true)
                    else
                        setupCountDown(false)
                }, onLoading = {
                    progressCircle.visibleOrGone(true)
                }, onErrors = { it ->
                    progressCircle.visibleOrGone(false)
                    if (!mainViewModel.sessionToken.isNullOrBlank())
                        handleApiError(it, retry = {
                            if (it is ErrorEntity.HttpError && it.httpError is HttpErrorEntity.Unauthorized401) {
                                setupCountDown(false)
                            } else
                                mainViewModel.loadProfile()
                        })
                    else
                        setupCountDown(false)
                }
            ))
        }
    }

    private fun FragmentSplashBinding.bindLogoutCollectResult() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.logoutStateFlow.stateFlow.collect(FlowEvent(
                onErrors = {
                    handleApiError(it) {
                        setupCountDown(true)
                    }
                }, onSuccess = {
                    progressCircle.visibleOrGone(false)
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

    override fun onStart() {
        super.onStart()
        countDownTimer?.start()
    }

    override fun onStop() {
        countDownTimer?.cancel()
        super.onStop()
    }
}