package com.eaapps.schoolsguide.features.search.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.databinding.FilterBottomSheetBinding
import com.eaapps.schoolsguide.features.search.Filter
import com.eaapps.schoolsguide.features.search.ShareViewModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@InternalCoroutinesApi
@AndroidEntryPoint
class FilterBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomSheetBinding
    private val shareViewModel: ShareViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by viewModels()
    private var pairProgram: Pair<List<ProgramsResponse.Programs>, ArrayList<String>>? = null
    private var pairGrades: Pair<List<GradesResponse.Grades>, ArrayList<String>>? = null
    private var pairCities: Pair<List<CityResponse.City>, ArrayList<String>>? = null

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.75f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindEvaluationFees()
        binding.bindListCitiesCollectData()
        binding.bindListGradesCollectData()
        binding.bindListProgramCollectData()
        binding.bindListSchoolType()
        binding.bindClicks()
    }

    private fun FilterBottomSheetBinding.bindEvaluationFees() {
        shareViewModel.filterModel.apply {
            schoolFees.sliderFees.setValues(
                from_price?.toFloat() ?: 0.0f,
                to_price?.toFloat() ?: 500000.0f
            )
            schoolFees.fromValue.setText("${schoolFees.sliderFees.values[0]}")
            schoolFees.toValue.setText("${schoolFees.sliderFees.values[1]}")
            evaluationValue.rating = this.review?.toFloat() ?: 0f
        }

        evaluationValue.setOnRatingBarChangeListener { _, rating, _ ->
            shareViewModel.filterModel.review = rating.toInt()
        }

        schoolFees.sliderFees.addOnChangeListener { rangeSlider, _, _ ->
            val valueFrom = rangeSlider.values[0].toInt()
            val valueTo = rangeSlider.values[1].toInt()
            schoolFees.fromValue.setText("$valueFrom")
            schoolFees.toValue.setText("$valueTo")
            shareViewModel.filterModel.from_price = valueFrom
            shareViewModel.filterModel.to_price = valueTo

        }

    }

    private fun FilterBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }

        clearAllBtn.setOnClickListener {
            shareViewModel.filterModel.clear()
            lifecycleScope.launch {
                shareViewModel.filterFire.emit(Resource.Success(Filter.CLEAR_FILTER))
            }
            bindClear()
        }

        applyFilter.setOnClickListener {
            lifecycleScope.launch {
                shareViewModel.filterFire.emit(Resource.Success(Filter.APPLY_FILTER))
            }
            dismiss()
        }
    }

    private fun FilterBottomSheetBinding.bindListProgramCollectData() {
        lifecycleScope.launchWhenCreated {
            filterViewModel.schoolProgramStateFlow.stateFlow.collect(FlowEvent(onLoading = {
                bindShimmerStartLoading()
            }, onError = {
                bindShimmerStopLoading()
            }, onSuccess = { it ->
                bindShimmerStopLoading()
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name)
                }
                pairProgram = Pair(it, list)
                pairProgram?.second?.apply {
                    curriculumType.listViewChoice.adapter =
                        ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

                    curriculumType.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                        curriculumType.itemSelect = this[position]
                        shareViewModel.filterModel.program_id = pairProgram?.first!![position].id
                        shareViewModel.filterModel.filterPosition.program_id = position
                    }
                    shareViewModel.filterModel.filterPosition.program_id?.let {
                        curriculumType.itemSelect = this[it]
                        curriculumType.listViewChoice.setSelection(it)
                        curriculumType.listViewChoice.setItemChecked(it, true)
                    }
                }
            }))
        }
    }

    private fun FilterBottomSheetBinding.bindListGradesCollectData() {
        lifecycleScope.launchWhenCreated {
            filterViewModel.schoolGradesStateFlow.stateFlow.collect(FlowEvent(onLoading = {
                bindShimmerStartLoading()
            }, onError = {
                bindShimmerStopLoading()
            }, onSuccess = { it ->
                bindShimmerStopLoading()
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name!!)
                }
                pairGrades = Pair(it, list)
                pairGrades?.second?.apply {
                    educationLevel.listViewChoice.adapter =
                        ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
                    educationLevel.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                        educationLevel.itemSelect = this[position]
                        shareViewModel.filterModel.grade_id = pairGrades?.first!![position].id
                        shareViewModel.filterModel.filterPosition.grade_id = position
                    }
                    shareViewModel.filterModel.filterPosition.grade_id?.let {
                        educationLevel.itemSelect = this[it]
                        educationLevel.listViewChoice.setSelection(it)
                        educationLevel.listViewChoice.setItemChecked(it, true)
                    }

                }

            }))
        }
    }

    private fun FilterBottomSheetBinding.bindListCitiesCollectData() {
        lifecycleScope.launchWhenCreated {
            filterViewModel.citiesStateFlow.stateFlow.collect(
                FlowEvent(onLoading = {
                    bindShimmerStartLoading()
                }, onError = {
                    bindShimmerStopLoading()
                }, onSuccess = { it ->
                    bindShimmerStopLoading()
                    val list: ArrayList<String> = ArrayList()
                    it.forEach { city ->
                        list.add(city.name)
                    }
                    pairCities = Pair(it, list)
                    pairCities?.second?.apply {
                        cities.listViewChoice.adapter =
                            ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

                        cities.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                            cities.itemSelect = this[position]
                            shareViewModel.filterModel.city_id =
                                pairCities?.first!![position].id
                            shareViewModel.filterModel.filterPosition.city_id = position
                        }

                        shareViewModel.filterModel.filterPosition.city_id?.let {
                            cities.itemSelect = this[it]
                            cities.listViewChoice.setSelection(it)
                            cities.listViewChoice.setItemChecked(it, true)
                        }
                    }
                })
            )
        }
    }

    private fun FilterBottomSheetBinding.bindListSchoolType() {
        requireContext().resources.getStringArray(R.array.schoolType).apply {
            typeSchool.listViewChoice.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

            typeSchool.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                typeSchool.itemSelect = this[position]
                shareViewModel.filterModel.school_type = this[position].toLowerCase(Locale.ENGLISH)
                shareViewModel.filterModel.filterPosition.school_type = position

            }

            shareViewModel.filterModel.filterPosition.school_type?.let {
                typeSchool.itemSelect = this[it]
                typeSchool.listViewChoice.setSelection(it)
                typeSchool.listViewChoice.setItemChecked(it, true)
            }
        }
    }

    private fun FilterBottomSheetBinding.bindClear() {
        typeSchool.itemSelect = ""
        educationLevel.itemSelect = ""
        cities.itemSelect = ""
        curriculumType.itemSelect = ""
        evaluationValue.rating = 0f
        schoolFees.sliderFees.setValues(0.0f, 500000.0f)
        schoolFees.fromValue.setText(getString(R.string._0))
        schoolFees.toValue.setText(getString(R.string._500000))
        typeSchool.listViewChoice.clearChoices()
        typeSchool.listViewChoice.requestLayout()

        educationLevel.listViewChoice.clearChoices()
        educationLevel.listViewChoice.requestLayout()

        cities.listViewChoice.clearChoices()
        cities.listViewChoice.requestLayout()

        curriculumType.listViewChoice.clearChoices()
        curriculumType.listViewChoice.requestLayout()
    }

    private fun FilterBottomSheetBinding.bindShimmerStartLoading() {
        filterShimmerLoading.groupShimmerFilter.startShimmer()
        filterShimmerLoading.groupShimmerFilter.visibleOrGone(true)
        groupFilter.visibleOrGone(false)
    }

    private fun FilterBottomSheetBinding.bindShimmerStopLoading() {
        filterShimmerLoading.groupShimmerFilter.stopShimmer()
        filterShimmerLoading.groupShimmerFilter.visibleOrGone(false)
        groupFilter.visibleOrGone(true)
    }

}