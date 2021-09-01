package com.eaapps.schoolsguide.features.profile.subfeature.updatePassword

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
import com.eaapps.schoolsguide.databinding.FragmentDialogUpdatePasswordBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@AndroidEntryPoint
@InternalCoroutinesApi
class UpdatePasswordFragment : DialogFragment(R.layout.fragment_dialog_update_password) {

    private val binding: FragmentDialogUpdatePasswordBinding by viewBinding(
        FragmentDialogUpdatePasswordBinding::bind
    )

    private val viewModel: UpdatePasswordViewModel by viewModels()

    private lateinit var dialogProcess: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = createDialog(R.style.AppTheme, Color.WHITE,true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updatePasswordViewModel = viewModel
        binding.executePendingBindings()
        dialogProcess = requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))

        binding.backBtn.setOnClickListener {
            dismiss()
        }

        updatePasswordResultCollect()

    }

    private fun updatePasswordResultCollect() {
        lifecycleScope.launchWhenCreated {
            viewModel.updatePasswordStateFlow.collect(FlowEvent(onError = {
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