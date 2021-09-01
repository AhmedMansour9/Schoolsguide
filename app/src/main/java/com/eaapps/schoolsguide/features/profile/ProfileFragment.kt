package com.eaapps.schoolsguide.features.profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentProfileBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainActivity
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        binding.executePendingBindings()
        mainViewModel.loadProfile()
        binding.bindProfile()
        binding.bindClicks()
    }

    private fun FragmentProfileBinding.bindProfile() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(
                onErrors = {
                    profileShimmer.shimmerLayout.stopShimmer()
                    groupProfile.visibleOrGone(false)
                    profileShimmer.shimmerLayout.visibleOrGone(false)
                    handleApiError(it) {
                        mainViewModel.loadProfile()
                    }
                }, onSuccess = {
                    profileShimmer.shimmerLayout.stopShimmer()
                    profileShimmer.shimmerLayout.visibleOrGone(false)
                    groupProfile.visibleOrGone(true)
                    authData = it
                }, onLoading = {
                    groupProfile.visibleOrGone(false)
                    profileShimmer.shimmerLayout.startShimmer()
                    profileShimmer.shimmerLayout.visibleOrGone(true)
                })
            )
        }
    }

    private fun FragmentProfileBinding.bindClicks() {
        booksRequest.profileItemGroup.setOnClickListener(this@ProfileFragment)
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

    private fun navigationToSplash() =
        launchFragment(ProfileFragmentDirections.actionProfileFragmentToSplashFragment())

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.booksRequest -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToOrderSchoolFragment())

            R.id.updateProfile -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment())

            R.id.updatePassword -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())

            R.id.changeLanguage -> {
                val array = arrayOf("en", "ar")
                var position = 0
                val checkItem = mainViewModel.loadLanguage()?.let {
                    array.indexOf(it)
                } ?: 0
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogStyle)
                    .setTitle(getString(R.string.choose_lang))
                    .setSingleChoiceItems(
                        resources.getStringArray(R.array.languages),
                        checkItem
                    ) { _, which ->
                        position = which
                    }
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                        mainViewModel.saveLanguage(array[position])
                        dialog.dismiss()
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            })
                    }
                    .create()
                    .show()
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


    private fun openWebPage(url: String) {
        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }


}

private const val TAG = "ProfileFragment"
