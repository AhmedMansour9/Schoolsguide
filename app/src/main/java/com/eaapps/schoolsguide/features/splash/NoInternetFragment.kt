package com.eaapps.schoolsguide.features.splash

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentNoInternetBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class NoInternetFragment : DialogFragment(R.layout.fragment_no_internet) {

    private val binding: FragmentNoInternetBinding by viewBinding(FragmentNoInternetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindCheckInternet()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = createDialog(
        R.style.AppTheme,
        Color.WHITE,
        true,
        shouldInterceptBackPress = true
    ) { }


    private fun FragmentNoInternetBinding.bindCheckInternet() {
        retryButton.setOnClickListener {
            progressBar.visibleOrGone(true)
            retryButton.visibleOrGone(false)
            lifecycleScope.launchWhenStarted {
                NetworkChecker.isOnline().collect(FlowEvent(onError = {
                    progressBar.visibleOrGone(false)
                    retryButton.visibleOrGone(true)
                }, onSuccess = {
                    if (it) {
                        progressBar.visibleOrGone(false)
                        retryButton.visibleOrGone(false)
                        msg.text = getString(R.string.online)
                        dismiss()
                    } else {
                        progressBar.visibleOrGone(false)
                        retryButton.visibleOrGone(true)
                    }
                }))
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        launchFragment(NoInternetFragmentDirections.actionNoInternetFragmentToSplashFragment())
    }
}