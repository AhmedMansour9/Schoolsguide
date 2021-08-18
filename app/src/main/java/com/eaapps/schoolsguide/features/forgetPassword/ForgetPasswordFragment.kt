package com.eaapps.schoolsguide.features.forgetPassword

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentForgetPasswordBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@InternalCoroutinesApi
@AndroidEntryPoint
class ForgetPasswordFragment : DialogFragment(R.layout.fragment_forget_password) {

    private val binding: FragmentForgetPasswordBinding by viewBinding(FragmentForgetPasswordBinding::bind)

    private val viewModel: ForgetPasswordViewModel by viewModels()

    private lateinit var dialogProcess: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forgetPasswordViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        bindArgs()
        binding.bindClicks()
        collectCreatePasswordDataFlow()
    }

    private fun bindArgs() {
        viewModel.email = ForgetPasswordFragmentArgs.fromBundle(requireArguments()).email
     }

    private fun FragmentForgetPasswordBinding.bindClicks() {
        back.setOnClickListener {
            dismiss()
        }
    }

    private fun collectCreatePasswordDataFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.createStateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
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
                    dialogProcess.show()
                },
                onSuccess = {
                    dialogProcess.dismiss()
                    MotionToast.createColorToast(
                        requireActivity(),
                        getString(R.string.check_your_mail),
                        getString(R.string.checkLabel_msg),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                    )
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        dialogProcess.dismiss()
        super.onDestroy()
    }

}