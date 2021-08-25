package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogJobViewBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobViewDialogFragment : DialogFragment(R.layout.fragment_dialog_job_view) {

    private val binding: FragmentDialogJobViewBinding by viewBinding(FragmentDialogJobViewBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(
            R.style.AppTheme,
            Color.WHITE,
            true,
            shouldInterceptBackPress = true
        ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        binding.bindArgs()
        binding.clicks()
    }

    private fun FragmentDialogJobViewBinding.bindArgs() {
        JobViewDialogFragmentArgs.fromBundle(requireArguments()).apply {
            jobData = dataJob

        }
    }

    private fun FragmentDialogJobViewBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }
}
