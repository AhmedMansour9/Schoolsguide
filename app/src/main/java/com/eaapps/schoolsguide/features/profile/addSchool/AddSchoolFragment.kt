package com.eaapps.schoolsguide.features.profile.addSchool

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogAddSchoolBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@AndroidEntryPoint
@InternalCoroutinesApi
class AddSchoolFragment : DialogFragment(R.layout.fragment_dialog_add_school) {

    private val binding: FragmentDialogAddSchoolBinding by viewBinding(
        FragmentDialogAddSchoolBinding::bind
    )

    private val viewModel: AddSchoolViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var dialogProcess: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.accessToken = mainViewModel.accessToken
        binding.addSchoolViewModel = viewModel

        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))

        addSchoolResultCollect()

        binding.backBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun addSchoolResultCollect() {
        lifecycleScope.launchWhenCreated {
            viewModel.addSchoolStateFlow.collect(FlowEvent(onError = {
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