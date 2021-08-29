package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.SchoolValuesBottomSheetBinding
import com.eaapps.schoolsguide.domain.model.ReviewModel
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@InternalCoroutinesApi
@AndroidEntryPoint
class SchoolValueBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: SchoolValuesBottomSheetBinding
    private lateinit var dialogProcess: Dialog

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SchoolValuesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(
            resources,
            0.75f,
            draggable = true,
            state = BottomSheetBehavior.STATE_COLLAPSED
        )
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        binding.executePendingBindings()
        buildArgs()
        binding.bindClicks()
        binding.bindRate()
        collectResultReview()

    }

    private fun buildArgs() {
        viewModel.reviewModel.school_id =
            SchoolValueBottomFragmentArgs.fromBundle(requireArguments()).schoolId
    }

    private fun SchoolValuesBottomSheetBinding.bindRate() {
        education.rateSeek.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.reviewModel.education = rating.toInt()
        }
        facilities.rateSeek.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.reviewModel.facilities = rating.toInt()
        }
        safety.rateSeek.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.reviewModel.safety = rating.toInt()
        }
        activities.rateSeek.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.reviewModel.activities = rating.toInt()
        }
        communication.rateSeek.setOnRatingBarChangeListener { _, rating, _ ->
            viewModel.reviewModel.communication = rating.toInt()
        }
    }

    private fun SchoolValuesBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun collectResultReview() {
        lifecycleScope.launchWhenStarted {
            viewModel.reviewSchoolFlow.stateFlow.collect(FlowEvent(onError = {
                viewModel.reviewSchoolFlow.setValue(Resource.Nothing())
                dialogProcess.dismiss()
               requireActivity().toastingError(it)
            },
                onLoading = {
                    dialogProcess.show()
                },
                onSuccess = {
                    MotionToast.createColorToast(
                        requireActivity(),
                        getString(R.string.school_value),
                        getString(R.string.successful_review),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        8000L,
                        ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                    )
                    viewModel.reviewSchoolFlow.setValue(Resource.Nothing())
                    dialogProcess.dismiss()
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.reviewModel = ReviewModel()
        super.onDismiss(dialog)
    }

}