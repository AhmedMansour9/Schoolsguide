package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ReviewParentsBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.ReviewParentsAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ReviewParentBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: ReviewParentsBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReviewParentsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.75f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding.detailsViewModel = viewModel
        binding.buildArgs()
        binding.bindClicks()

    }


    private fun ReviewParentsBottomSheetBinding.buildArgs() {
        ReviewParentBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcComments.adapter = ReviewParentsAdapter(this.schoolReviews)
        }
    }

    private fun ReviewParentsBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}