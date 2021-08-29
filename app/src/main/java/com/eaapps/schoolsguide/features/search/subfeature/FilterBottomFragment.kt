package com.eaapps.schoolsguide.features.search.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.databinding.FilterBottomSheetBinding
import com.eaapps.schoolsguide.features.search.viewmodels.Filter
import com.eaapps.schoolsguide.features.search.viewmodels.FilterViewModel
import com.eaapps.schoolsguide.features.search.viewmodels.ShareViewModel
import com.eaapps.schoolsguide.utils.ObserveEvent
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@InternalCoroutinesApi
@AndroidEntryPoint
class FilterBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FilterBottomSheetBinding
    private val shareViewModel: ShareViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)
    }

    private val filterViewModel: FilterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
    }
    private var pairProgram: Pair<List<ProgramsResponse.Programs>, ArrayList<String>>? = null
    private var pairGrades: Pair<List<GradesResponse.Grades>, ArrayList<String>>? = null
    private var pairCities: Pair<List<CityResponse.City>, ArrayList<String>>? = null
    private var pairTypes: Pair<List<TypeResponse.TypeData>, ArrayList<String>>? = null
    private var typeSearch = 0

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
        setupArgs()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.bindEvaluationFees()
        binding.bindListCategoryCollectData()
        binding.bindListCitiesCollectData()
        binding.bindListGradesCollectData()
        binding.bindListProgramCollectData()
        binding.bindClicks()
    }

    private fun setupArgs() {
        FilterBottomFragmentArgs.fromBundle(requireArguments()).apply {
            typeSearch = type
        }
    }

    private fun FilterBottomSheetBinding.bindClear() {
        shareViewModel.filterModel.clearAll()
        bindClearListCategory()
        bindClearListCity()
        bindClearListEducationLevel()
        bindClearListCurriculum()
        bindClearListType()
        bindClearValuation()

        if (typeSearch == 0)
            shareViewModel.filterFire.value = Resource.Success(Filter.CLEAR_FILTER)
        else
            shareViewModel.filterMapFire.value = Resource.Success(Filter.CLEAR_FILTER)
    }

    private fun FilterBottomSheetBinding.bindClicks() {

        cancelBtn.setOnClickListener {
            bindClear()
            dismiss()
        }

        clearAllBtn.setOnClickListener {
            bindClear()
        }

        applyFilter.setOnClickListener {
            if (typeSearch == 0) {
                shareViewModel.filterFire.value = Resource.Success(Filter.APPLY_FILTER)
            } else {
                shareViewModel.filterMapFire.value = Resource.Success(Filter.APPLY_FILTER)
            }
            dismiss()
        }

        schoolCategory.clearBtn.setOnClickListener {
            bindClearListCategory()
        }

        cities.clearBtn.setOnClickListener {
            bindClearListCity()
        }

        educationLevel.clearBtn.setOnClickListener {
            bindClearListEducationLevel()
        }

        curriculumType.clearBtn.setOnClickListener {
            bindClearListCurriculum()
        }

        typeSchool.clearBtn.setOnClickListener {
            bindClearListType()
        }
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

    private fun FilterBottomSheetBinding.bindListProgramCollectData() {
        val curriculumListView = curriculumType.listViewChoice
        filterViewModel.schoolProgramLiveData.observe(viewLifecycleOwner, ObserveEvent(
            onLoading = {
                bindShimmerStartLoading()
            }, onError = {
                bindShimmerStopLoading()
            }, onSuccess = { it ->
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name)
                }
                bindShimmerStopLoading()
                pairProgram = Pair(it, list)
                pairProgram?.second?.apply {
                    curriculumListView.adapter =
                        ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
                    curriculumListView.setOnItemClickListener { _, _, position, _ ->
                        curriculumType.itemSelect = this[position]
                        shareViewModel.filterModel.program_id =
                            pairProgram?.first!![position].id
                        shareViewModel.filterModel.filterPosition.program_id = position
                    }

                    // Load Current Program Select
                    shareViewModel.filterModel.filterPosition.program_id?.let {
                        curriculumType.itemSelect = this[it]
                        curriculumListView.setSelection(it)
                        curriculumListView.setItemChecked(it, true)
                    }
                }
            })
        )
    }

    private fun FilterBottomSheetBinding.bindListGradesCollectData() {
        val educationList = educationLevel.listViewChoice
        filterViewModel.schoolGradesLiveData.observe(
            viewLifecycleOwner,
            ObserveEvent(
                onLoading = {
                    bindShimmerStartLoading()
                },
                onError = {
                    bindShimmerStopLoading()
                },
                onSuccess = { it ->
                    val list: ArrayList<String> = ArrayList()
                    it.forEach { typeData ->
                        list.add(typeData.name!!)
                    }
                    bindShimmerStopLoading()
                    pairGrades = Pair(it, list)
                    pairGrades?.second?.apply {
                        educationList.adapter =
                            ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
                        educationList.setOnItemClickListener { _, _, position, _ ->
                            educationLevel.itemSelect = this[position]
                            shareViewModel.filterModel.grade_id =
                                pairGrades?.first!![position].id
                            shareViewModel.filterModel.filterPosition.grade_id = position
                        }
                        //Load Current Grade
                        shareViewModel.filterModel.filterPosition.grade_id?.let {
                            educationLevel.itemSelect = this[it]
                            educationList.setSelection(it)
                            educationList.setItemChecked(it, true)
                        }
                    }
                })
        )
    }

    private fun FilterBottomSheetBinding.bindListCitiesCollectData() {
        val cityList = cities.listViewChoice
        filterViewModel.citiesLiveData.observe(
            viewLifecycleOwner,
            ObserveEvent(
                onLoading = {
                    bindShimmerStartLoading()
                },
                onError = {
                    bindShimmerStopLoading()
                },
                onSuccess = { it ->
                    val list: ArrayList<String> = ArrayList()
                    it.forEach { city ->
                        list.add(city.name)
                    }
                    bindShimmerStopLoading()
                    pairCities = Pair(it, list)
                    pairCities?.second?.apply {
                        cityList.adapter =
                            ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
                        cityList.setOnItemClickListener { _, _, position, _ ->
                            cities.itemSelect = this[position]
                            shareViewModel.filterModel.city_id =
                                pairCities?.first!![position].id
                            shareViewModel.filterModel.filterPosition.city_id = position
                        }
                        shareViewModel.filterModel.filterPosition.city_id?.let {
                            cities.itemSelect = this[it]
                            cityList.setSelection(it)
                            cityList.setItemChecked(it, true)
                        }
                    }
                })
        )
    }

    private fun FilterBottomSheetBinding.bindListCategoryCollectData() {
        val categoryList = schoolCategory.listViewChoice
        filterViewModel.schoolTypeLiveData.observe(
            viewLifecycleOwner,
            ObserveEvent(
                onLoading = {
                    bindShimmerStartLoading()
                },
                onError = {
                    bindShimmerStopLoading()
                },
                onSuccess = { it ->
                    val list: ArrayList<String> = ArrayList()
                    it.forEach { typeData ->
                        list.add(typeData.name)
                    }
                    bindShimmerStopLoading()
                    pairTypes = Pair(it, list)
                    pairTypes?.second?.apply {
                        categoryList.adapter =
                            ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
                        categoryList.setOnItemClickListener { _, _, position, _ ->
                            schoolCategory.itemSelect = this[position]
                            shareViewModel.filterModel.type_id = pairTypes?.first!![position].id
                            shareViewModel.filterModel.filterPosition.type_id = position
                        }
                        shareViewModel.filterModel.filterPosition.type_id?.also {
                            schoolCategory.itemSelect = this[it]
                            categoryList.setSelection(it)
                            categoryList.setItemChecked(it, true)
                        }
                    }
                    bindListSchoolType()
                })
        )
    }

    private fun FilterBottomSheetBinding.bindListSchoolType() {
        val typeList = typeSchool.listViewChoice
        resources.getStringArray(R.array.schoolType).apply {
            typeList.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
            typeList.setOnItemClickListener { _, _, position, _ ->
                val array = arrayOf("males", "females", "males_females")
                typeSchool.itemSelect = this[position]
                shareViewModel.filterModel.school_type = array[position].toLowerCase(Locale.ENGLISH)
                shareViewModel.filterModel.filterPosition.school_type = position
            }
            shareViewModel.filterModel.filterPosition.school_type?.let {
                typeSchool.itemSelect = this[it]
                typeList.setSelection(it)
                typeList.setItemChecked(it, true)
            }
        }
    }

    private fun FilterBottomSheetBinding.bindClearValuation() {
        evaluationValue.rating = 0f
        schoolFees.sliderFees.setValues(0.0f, 500000.0f)
        schoolFees.fromValue.setText(getString(R.string._0))
        schoolFees.toValue.setText(getString(R.string._500000))
    }

    private fun FilterBottomSheetBinding.bindClearListCategory() {
        shareViewModel.filterModel.clearCategory()
        schoolCategory.itemSelect = ""
        schoolCategory.listViewChoice.clearChoices()
        schoolCategory.listViewChoice.requestLayout()
    }

    private fun FilterBottomSheetBinding.bindClearListCity() {
        shareViewModel.filterModel.clearCity()
        cities.itemSelect = ""
        cities.listViewChoice.clearChoices()
        cities.listViewChoice.requestLayout()
    }

    private fun FilterBottomSheetBinding.bindClearListCurriculum() {
        shareViewModel.filterModel.clearProgram()
        curriculumType.itemSelect = ""
        curriculumType.listViewChoice.clearChoices()
        curriculumType.listViewChoice.requestLayout()
    }

    private fun FilterBottomSheetBinding.bindClearListType() {
        shareViewModel.filterModel.clearType()
        typeSchool.itemSelect = ""
        typeSchool.listViewChoice.clearChoices()
        typeSchool.listViewChoice.requestLayout()
    }

    private fun FilterBottomSheetBinding.bindClearListEducationLevel() {
        shareViewModel.filterModel.clearEducationLevel()
        educationLevel.itemSelect = ""
        educationLevel.listViewChoice.clearChoices()
        educationLevel.listViewChoice.requestLayout()
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