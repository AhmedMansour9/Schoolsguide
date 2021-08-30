package com.eaapps.schoolsguide.features.register

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentRegisterBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register), RegisterNavigator {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel.registerNavigator = this
        binding.registerViewModel = registerViewModel
        dialog =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        binding.bindCityCollectList()
        collectRegisterFlow()
        binding.bindClicks()

    }

    private fun FragmentRegisterBinding.bindClicks() {
        cityEditRegister.setOnItemClickListener { _, _, position, _ ->
            registerViewModel?.registerModel!!.city = position + 1
        }
    }

    private fun FragmentRegisterBinding.bindCityCollectList() {
        lifecycleScope.launchWhenCreated {
            registerViewModel?.citiesStateFlow!!.collect(
                FlowEvent(
                    onErrors = {
                        handleApiError(it) {
                            registerViewModel?.loadCities()!!
                        }
                    },
                    onSuccess = {
                        val adapter = ArrayAdapter(requireContext(), R.layout.city_list_item, it)
                        cityEditRegister.setAdapter(adapter)
                    })
            )
        }
    }

    private fun collectRegisterFlow() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerStateFlow.collect(FlowEvent(
                onErrors = {
                    dialog.dismiss()
                    handleApiError(it) {
                        registerViewModel.register()
                    }
                },
                onLoading = {
                    dialog.show()
                },
                onSuccess = {
                    dialog.dismiss()
                    launchFragment(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
                },
                onNothing = { dialog.dismiss() }
            ))
        }
    }

    override fun loginNow() {
        findNavController().navigateUp()
    }

}