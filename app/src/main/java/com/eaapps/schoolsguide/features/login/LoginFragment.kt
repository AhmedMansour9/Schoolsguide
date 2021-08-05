package com.eaapps.schoolsguide.features.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentLoginBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.progressDialog
import com.eaapps.schoolsguide.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@AndroidEntryPoint
@InternalCoroutinesApi

class LoginFragment : Fragment(R.layout.fragment_login), LoginNavigator {

    private val loginViewModel: LoginViewModel by viewModels()
    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private lateinit var dialog: Dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.navigator = this
        binding.loginViewModel = loginViewModel
        dialog = requireContext().progressDialog(Color.BLACK)
        collectLoginFlow()

    }

    @SuppressLint("TimberArgCount")
    private fun collectLoginFlow() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginStateFlow.collect(FlowEvent(onError = {
                dialog.dismiss()
                snackbar(it)
            },
                onLoading = {
                    dialog.show()
                },
                onSuccess = {
                    Log.d("edase", "collectLoginFlow: ${it.full_name}")
                    Timber.d("Full name", it.full_name)
                    dialog.dismiss()
                }
            ))
        }
    }

    override fun registerNow() {

    }

    override fun googleLogin() {

    }

    override fun facebookLogin() {

    }

    override fun onDestroy() {
        dialog.dismiss()
        super.onDestroy()
    }
}