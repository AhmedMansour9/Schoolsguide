package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.TuitionFeesBottomSheetBinding
import com.eaapps.schoolsguide.domain.model.SubModel
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.Sub2Adapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class TuitionFeesBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: TuitionFeesBottomSheetBinding

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TuitionFeesBottomSheetBinding.inflate(inflater, container, false)
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


    private fun TuitionFeesBottomSheetBinding.buildArgs() {
        TuitionFeesBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcStatics.adapter = Sub2Adapter(
                this.tuition_fees.let { it ->
                    val data = ArrayList<SubModel>()
                    data.add(SubModel("Classroom", "Study fees", true))
                    it.forEach {
                        it.grade?.name?.apply {
                            data.add(SubModel(this, "${it.total}", false))
                        }
                    }
                    data
                }
            )

            rcStatics.visibleOrGone(this.tuition_fees.isNotEmpty())
            noItem.run {
                noItem.groupNo.visibleOrGone(tuition_fees.isEmpty())
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                titleNo = getString(R.string.tuition_no_msg)
            }
        }
    }

    private fun TuitionFeesBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}