package com.eaapps.schoolsguide.features.profile

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentProfileBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi


@AndroidEntryPoint
@InternalCoroutinesApi
class ProfileFragment : Fragment(R.layout.fragment_profile), View.OnClickListener {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var dialogProcess: Dialog
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogProcess = requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        binding.executePendingBindings()
        mainViewModel.loadProfile()
        binding.bindProfile()
        binding.bindClicks()
    }

    private fun FragmentProfileBinding.bindProfile() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(onError = {
                profileShimmer.shimmerLayout.stopShimmer()
                profileShimmer.shimmerLayout.visibleOrGone(false)
                requireActivity().toastingError(it)
            }, onSuccess = {
                profileShimmer.shimmerLayout.stopShimmer()
                profileShimmer.shimmerLayout.visibleOrGone(false)
                groupProfile.visibleOrGone(true)
                authData = it
            }, onLoading = {
                groupProfile.visibleOrGone(false)
                profileShimmer.shimmerLayout.startShimmer()
                profileShimmer.shimmerLayout.visibleOrGone(true)
            }))
        }
    }

    private fun FragmentProfileBinding.bindClicks() {
        addSchool.profileItemGroup.setOnClickListener(this@ProfileFragment)
        updateProfile.profileItemGroup.setOnClickListener(this@ProfileFragment)
        updatePassword.profileItemGroup.setOnClickListener(this@ProfileFragment)
        changeLanguage.profileItemGroup.setOnClickListener(this@ProfileFragment)
        logout.profileItemGroup.setOnClickListener(this@ProfileFragment)
    }

    private fun logoutResultCollect() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.logoutStateFlow.stateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
                requireActivity().toastingError(it)
            },
                onLoading = {
                    dialogProcess.show()
                },
                onSuccess = {
                    dialogProcess.dismiss()
                    navigationToSplash()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }

    private fun navigationToSplash() = launchFragment(ProfileFragmentDirections.actionProfileFragmentToSplashFragment())

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addSchool -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToAddSchoolFragment())

            R.id.updateProfile -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment())

            R.id.updatePassword -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())

            R.id.changeLanguage -> {

            }

            R.id.logout -> {
                firebaseAuth.currentUser?.apply {
                    firebaseAuth.signOut()
                    GoogleSignIn.getClient(
                        requireContext(),
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    ).signOut()
                }
                mainViewModel.logoutFather()
                logoutResultCollect()
            }

        }
    }

    override fun onDestroy() {
        dialogProcess.dismiss()
        super.onDestroy()
    }

}