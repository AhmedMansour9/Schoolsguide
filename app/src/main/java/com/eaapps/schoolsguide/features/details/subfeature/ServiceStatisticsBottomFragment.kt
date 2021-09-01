package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.ServiceStatisticsBottomSheetBinding
import com.eaapps.schoolsguide.domain.model.SubModel
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.SubAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class ServiceStatisticsBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ServiceStatisticsBottomSheetBinding

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ServiceStatisticsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.60f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buildArgs()
        binding.bindClicks()

    }


    private fun ServiceStatisticsBottomSheetBinding.buildArgs() {
        ServiceStatisticsBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcStatics.adapter = SubAdapter(
                arrayListOf(
                    SubModel(
                        "Average number of students per class",
                        average_number_of_students_per_class
                    ),
                    SubModel("Number of Employees", number_of_employees ?: "0"),
                    SubModel("Number of labs", number_of_labs ?: "0"),
                    SubModel("Number of libraries", number_of_libraries ?: "0"),
                    SubModel("Number of football fields", number_of_football_fields ?: "0"),
                    SubModel("Number of volleyball courts", number_of_volleyball_courts ?: "0"),
                    SubModel(
                        "Number of basketball courts",
                        number_of_basketball_courts ?: "0",
                        false
                    )
                )
            )
        }
    }

    private fun ServiceStatisticsBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}