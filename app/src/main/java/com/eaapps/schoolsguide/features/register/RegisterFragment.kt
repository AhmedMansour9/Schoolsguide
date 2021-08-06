package com.eaapps.schoolsguide.features.register

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentRegisterBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

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
        collectCities()
        dialog =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        collectRegisterFlow()
        binding.cityEditRegister.setOnItemClickListener { _, _, position, _ ->
            registerViewModel.registerModel.city = position + 1
        }
    }

    private fun collectCities() {
        lifecycleScope.launchWhenCreated {
            registerViewModel.citiesStateFlow.collect(
                FlowEvent(onError = {},
                    onSuccess = {
                        val adapter = ArrayAdapter(requireContext(), R.layout.city_list_item, it)
                        binding.cityEditRegister.setAdapter(adapter)
                    })
            )
        }
    }

    private fun collectRegisterFlow() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerStateFlow.collect(FlowEvent(onError = {
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
                },
                onNothing = { dialog.dismiss() }
            ))
        }
    }

    override fun loginNow() {
        findNavController().navigateUp()
    }


}