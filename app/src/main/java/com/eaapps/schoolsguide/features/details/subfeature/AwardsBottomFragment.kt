package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.AdwardsBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.AwardsAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class AwardsBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: AdwardsBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AdwardsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.70f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        binding.buildArgs()
        binding.bindClicks()

    }


    private fun AdwardsBottomSheetBinding.buildArgs() {
        AwardsBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcAwards.layoutManager = GridLayoutManager(requireContext(), 2)
            rcAwards.adapter = AwardsAdapter(this.awards)
            noItem.run {
                noItem.groupNo.visibleOrGone(awards.isEmpty())
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                titleNo = getString(R.string.awards_no_msg)
            }
        }
    }

    private fun AdwardsBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}