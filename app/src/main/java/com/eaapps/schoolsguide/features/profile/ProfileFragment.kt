package com.eaapps.schoolsguide.features.profile

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentProfileBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@AndroidEntryPoint
@InternalCoroutinesApi
class ProfileFragment : Fragment(R.layout.fragment_profile), View.OnClickListener {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var dialogProcess: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.executePendingBindings()
        loadProfile()
         dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))

        binding.addSchool.profileItemGroup.setOnClickListener(this)
        binding.updateProfile.profileItemGroup.setOnClickListener(this)
        binding.updatePassword.profileItemGroup.setOnClickListener(this)
        binding.changeLanguage.profileItemGroup.setOnClickListener(this)
        binding.logout.profileItemGroup.setOnClickListener(this)

    }

    private fun loadProfile() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.profileStateFlow.collect(FlowEvent(onError = {

            }, onSuccess = {
                binding.nameFather.text = it.full_name
                binding.emailFather.text = it.email

            }))
        }

    }

    private fun logoutResultCollect() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.logoutStateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
                MotionToast.createColorToast(
                    requireActivity(),
                    "Failed ☹",
                    it,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)

                )
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