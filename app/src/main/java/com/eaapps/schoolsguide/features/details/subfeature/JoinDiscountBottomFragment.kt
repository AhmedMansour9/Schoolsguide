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
import com.eaapps.schoolsguide.databinding.JoinDiscountBottomSheetBinding
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
class JoinDiscountBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: JoinDiscountBottomSheetBinding
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private lateinit var dialogProcess: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JoinDiscountBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            this.createDialog(resources, .80f)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailsViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        buildArgs()
        collectResultJoinDiscount()
        binding.bindListsDown()
        binding.bindClicks()
    }

    private fun buildArgs() {
        viewModel.joinDiscountModel.school_id =
            BookNowBottomFragmentArgs.fromBundle(requireArguments()).schoolId
    }

    private fun JoinDiscountBottomSheetBinding.bindListsDown() {
        val arrayItem = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val adapterNumber = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            arrayItem
        )
        (studentNumberEdit as? AutoCompleteTextView)?.setAdapter(adapterNumber)
        (studentNumberEdit as? AutoCompleteTextView)?.setOnItemClickListener { parent, view, position, id ->
            viewModel.joinDiscountModel.number_of_students = arrayItem[position]
        }

    }

    private fun JoinDiscountBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun collectResultJoinDiscount() {
        lifecycleScope.launchWhenStarted {
            viewModel.joinDiscountFlow.stateFlow.collect(FlowEvent(onError = {
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
                        getString(R.string.join_discountLabel),
                        getString(R.string.join_successful),
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