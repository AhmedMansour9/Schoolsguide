package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.AddInquiryBottomSheetBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.getColorResource
import com.eaapps.schoolsguide.utils.progressSmallDialog
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
            this.createDialog(resources, .50f)
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
        viewModel.joinDiscountModel.school_id =
            BookNowBottomFragmentArgs.fromBundle(requireArguments()).schoolId
    }

    private fun AddInquiryBottomSheetBinding.bindListsDown() {
        val typeArray = resources.getStringArray(R.array.type_message)
        val adapterType = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            typeArray
        )

        val typeMethod = resources.getStringArray(R.array.replay_method)
        val adapterMethod = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            typeMethod
        )

        val replayTimeArray = resources.getStringArray(R.array.replay_time)

        val adapterReplayTime = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            replayTimeArray
        )

        (typeMessageEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterType)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.message_type = typeArray[position]
            }
        }
        (replayMessageEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterMethod)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.reply_type = typeMethod[position]
            }
        }
        (replayTimeEdit as? AutoCompleteTextView)?.apply {
            setAdapter(adapterReplayTime)
            setOnItemClickListener { _, _, position, _ ->
                viewModel.inquiryModel.reply_time = replayTimeArray[position]
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
}