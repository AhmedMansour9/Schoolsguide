package com.eaapps.schoolsguide.features.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentLoginBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
@InternalCoroutinesApi
class LoginFragment : Fragment(R.layout.fragment_login), LoginNavigator {

    private val loginViewModel: LoginViewModel by viewModels()
    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private lateinit var callbackManager: CallbackManager
    private lateinit var dialog: Dialog
    private lateinit var resultIntentSignIn: ActivityResultLauncher<Intent>
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        resultIntentSignIn =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.apply {
                        dialog.show()
                        firebaseSocialAuthentication(this, "google")
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.navigator = this
        binding.loginViewModel = loginViewModel
        dialog =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        configureFacebookLogin()
        configureGoogleSignIn()
        collectResultLoginFlow()
        collectResultLoginBySocialFlow()
    }

    private fun collectResultLoginFlow() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginStateFlow.stateFlow.collect(FlowEvent(onError = {
                dialog.dismiss()
                requireActivity().toastingError(it)
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

    private fun collectResultLoginBySocialFlow() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginByGoogleStateFlow.stateFlow.collect(FlowEvent(onError = {
                dialog.dismiss()
                requireActivity().toastingError(it)
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

    private fun configureFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    result?.accessToken?.apply {
                        dialog.show()
                        firebaseSocialAuthentication(token, "facebook")
                    }
                }

                override fun onCancel() {
                    dialog.dismiss()
                }

                override fun onError(error: FacebookException?) {
                    dialog.dismiss()
                    error?.message?.apply {
                        requireActivity().toastingError(this)
                    }

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun configureGoogleSignIn() {
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun firebaseSocialAuthentication(token: String, provider: String) {
        val credential: AuthCredential = if (provider == "facebook")
            FacebookAuthProvider.getCredential(token)
        else
            GoogleAuthProvider.getCredential(token, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.apply {
                        loginViewModel.socialModel.email = email!!
                        loginViewModel.socialModel.fullName = this.displayName!!
                        loginViewModel.socialModel.provider = provider
                        loginViewModel.socialModel.social_id = this.uid
                    }
                    user?.apply {
                        loginViewModel.loginBySocial()
                    }
                } else {
                    it.exception?.message?.apply {
                        requireActivity().toastingError(this)
                    }

                }
            }
    }

    private fun navigateToForgetPassword() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment(binding.emailEditLogin.text.toString()))

    private fun navigateToHome() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToHomeFragment())

    override fun registerNow() =
        launchFragment(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

    override fun googleLogin() = resultIntentSignIn.launch(googleSignInClient.signInIntent)

    override fun facebookLogin() =
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))

    override fun forgetPassword() = navigateToForgetPassword()

    override fun onDestroy() {
        dialog.dismiss()
        super.onDestroy()
    }
}