package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.AddInquiryBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@InternalCoroutinesApi
@AndroidEntryPoint
class AddInquiryBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: AddInquiryBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private lateinit var dialogProcess: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddInquiryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            this.createDialog(resources, 0.90f)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        buildArgs()
        collectResultSendInquiry()
        binding.bindListsDown()
        binding.bindClicks()
    }

    private fun buildArgs() {
        viewModel.inquiryModel.school_id =
            AddInquiryBottomFragmentArgs.fromBundle(requireArguments()).schoolId
    }

    private fun AddInquiryBottomSheetBinding.bindListsDown() {
        val typeArray = Pair(
            resources.getStringArray(R.array.type_message),
            arrayOf("question", "enquery", "complaint")
        )
        val adapterType = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            typeArray.first
        )

        val typeMethod = Pair(
            resources.getStringArray(R.array.replay_method),
            arrayOf("email", "message", "phone")
        )
        val adapterMethod = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            typeMethod.first
        )

        val replayTimeArray = Pair(
            resources.getStringArray(R.array.replay_time),
            arrayOf("any_time ", "morning_time ", "evening_time")
        )

        val adapterReplayTime = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            replayTimeArray.first
        )

        (typeMessageEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterType)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.message_type = typeArray.second[position]
            }
        }
        (replayMessageEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterMethod)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.reply_type = typeMethod.second[position]
            }
        }
        (replayTimeEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterReplayTime)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.reply_time = replayTimeArray.second[position]
            }
        }

    }

    private fun AddInquiryBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun collectResultSendInquiry() {
        lifecycleScope.launchWhenStarted {
            viewModel.sendInquiryFlow.stateFlow.collect(FlowEvent(onError = {
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
                    MotionToast.createColorToast(
                        requireActivity(),
                        getString(R.string.add_inquiry_label),
                        getString(R.string.inquiry_successfully),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        8000L,
                        ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                    )
                    viewModel.sendInquiryFlow.setValue(Resource.Nothing())
                    dialogProcess.dismiss()
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }

}