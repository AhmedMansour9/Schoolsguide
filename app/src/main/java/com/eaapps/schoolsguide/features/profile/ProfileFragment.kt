package com.eaapps.schoolsguide.features.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentProfileBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.launchFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
@InternalCoroutinesApi
class ProfileFragment : Fragment(R.layout.fragment_profile), View.OnClickListener {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.executePendingBindings()
        loadProfile()

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addSchool -> {
            }

            R.id.updateProfile -> {

            }

            R.id.updatePassword -> launchFragment(ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment())

            R.id.changeLanguage -> {

            }
            R.id.logout -> {

            }

        }
    }


}