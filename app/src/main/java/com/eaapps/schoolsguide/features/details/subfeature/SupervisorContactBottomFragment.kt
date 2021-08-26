package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.SupervisorContactBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.SupervisorAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class SupervisorContactBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: SupervisorContactBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SupervisorContactBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.55f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        binding.buildArgs()
        binding.bindClicks()

    }


    private fun SupervisorContactBottomSheetBinding.buildArgs() {
        SupervisorContactBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcSupervisor.adapter = SupervisorAdapter(this.communicates)
            contact.text = dates_for_communicating_with_supervisors
            contact.visibleOrGone(!dates_for_communicating_with_supervisors.isNullOrBlank())
            noItem.run {
                noItem.groupNo.visibleOrGone(communicates.isEmpty())
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_contact)
                titleNo = getString(R.string.supervisors_no_msg)
            }

        }
    }

    private fun SupervisorContactBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}