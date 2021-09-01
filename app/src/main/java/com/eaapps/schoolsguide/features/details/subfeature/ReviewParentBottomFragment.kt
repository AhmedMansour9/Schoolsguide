package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ReviewParentsBottomSheetBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.ReviewParentsAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ReviewParentBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: ReviewParentsBottomSheetBinding

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
        binding.buildArgs()
        binding.bindClicks()
    }

    private fun ReviewParentsBottomSheetBinding.buildArgs() {
        ReviewParentBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcComments.adapter = ReviewParentsAdapter(this.schoolReviews)
            noItem.run {
                noItem.groupNo.visibleOrGone(schoolReviews.isEmpty())
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                titleNo = getString(R.string.comment_no_msg)
            }
        }
    }

    private fun ReviewParentsBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}