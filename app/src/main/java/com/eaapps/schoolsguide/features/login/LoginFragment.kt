package com.eaapps.schoolsguide.features.login

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentLoginBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

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
        dialog =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        collectLoginFlow()
    }

    private fun collectLoginFlow() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginStateFlow.collect(FlowEvent(onError = {
                dialog.dismiss()
                MotionToast.createColorToast(
                    requireActivity(),
                    "Failed ☹",
                    it,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)

                )
            },
                onLoading = {
                    dialog.show()
                },
                onSuccess = {
                    dialog.dismiss()
                    navigateToHome()
                },
                onNothing = { dialog.dismiss() }
            ))
        }
    }


    private fun navigateToForgetPassword() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment(binding.emailEditLogin.text.toString()))


    private fun navigateToHome() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToHomeFragment())

    override fun registerNow() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

    override fun googleLogin() {

    }

    override fun facebookLogin() {

    }

    override fun forgetPassword() = navigateToForgetPassword()

    override fun onDestroy() {
        dialog.dismiss()
        super.onDestroy()
    }
}