package com.eaapps.schoolsguide.features.search

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.CityResponse
import com.eaapps.schoolsguide.data.entity.GradesResponse
import com.eaapps.schoolsguide.data.entity.ProgramsResponse
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.databinding.FilterBottomSheetBinding
import com.eaapps.schoolsguide.databinding.FragmentSearchBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.features.favorite.PagingStateLoading
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.hiddenKeyboard
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private var searchType: SearchType? = null
    private var listMode = true
    private var checkItem = 0
    private var pairTypes: Pair<List<TypeResponse.TypeData>, ArrayList<String>>? = null
    private var pairProgram: Pair<List<ProgramsResponse.Programs>, ArrayList<String>>? = null
    private var pairGrades: Pair<List<GradesResponse.Grades>, ArrayList<String>>? = null
    private var pairCities: Pair<List<CityResponse.City>, ArrayList<String>>? = null

    private val schoolPagingAdapter = SchoolPagingAdapter {
        viewModel.filterModel.city_id = 1
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArg()
        collectCitiesData()
        collectSchoolTypeData()
        collectSchoolGradesData()
        collectSchoolProgramData()
        binding.bindListChangeMode(listMode)
        binding.bindState(schoolPagingAdapter)
        binding.bindList(schoolPagingAdapter)
        binding.bindSearch()
        binding.bindClicks()
        loadListData()
    }

    private fun loadListData() {
        searchType?.apply {
            when {
                this.type > -1 -> {
                    viewModel.filterModel.type_id = type
                    collectTypedSchoolPagingData(type)
                }
                this.recommended -> {
                    collectAllRecommendedPagingData()
                }
                this.featured -> {
                    collectAllFeaturedPagingData()
                }
                else -> {

                }
            }
        }
    }

    private fun setupArg() {
        searchType = SearchFragmentArgs.fromBundle(requireArguments()).searchType
    }

    private fun FragmentSearchBinding.bindList(schoolPagingAdapter: SchoolPagingAdapter) {
        lifecycleScope.launchWhenCreated {
            schoolPagingAdapter.loadStateFlow.collect {
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && schoolPagingAdapter.itemCount == 0
                // show empty list Text
                // only show the list if refresh succeeds
                schoolsList.visibleOrGone(!isListEmpty)
                // Show loading spinner during initial load or refresh.
                progressBar.visibleOrGone(it.source.refresh is LoadState.Loading)
                // Show the retry state if initial load or refresh fails.
                retryButton.visibleOrGone(it.source.refresh is LoadState.Error)

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
        }
    }

    private fun FragmentSearchBinding.bindListChangeMode(listMode: Boolean) {
        if (listMode) {
            modeListBtn.setImageResource(R.drawable.ic_round_table_rows_24)
            schoolPagingAdapter.changeMode(listMode)
            schoolsList.layoutManager = LinearLayoutManager(requireContext())
        } else {
            modeListBtn.setImageResource(R.drawable.ic_grid_view_black_24dp__1_)
            schoolPagingAdapter.changeMode(listMode)
            schoolsList.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun FragmentSearchBinding.bindState(schoolPagingAdapter: SchoolPagingAdapter) {
        schoolsList.adapter = schoolPagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateLoading { schoolPagingAdapter.retry() },
            footer = PagingStateLoading { schoolPagingAdapter.retry() }
        )
    }

    private fun FragmentSearchBinding.bindSearch() {
        exploreField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else
                false
        }

        exploreField.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else
                false
        }
    }

    private fun FragmentSearchBinding.bindClicks() {
        modeListBtn.setOnClickListener {
            listMode = !listMode
            bindListChangeMode(listMode)
        }

        typeSchoolChoose.setOnClickListener {
            binding.bindDialogTypeSchool()
        }

        filterBtn.setOnClickListener {
            val binding = FilterBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
            binding.bindBottomSheet()
        }
    }

    private fun FragmentSearchBinding.bindDialogTypeSchool() {
        pairTypes?.second?.apply {
            val dialogChoose =
                MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogStyle)
            dialogChoose.setTitle("Choose Type")
            if (!this.contains("None"))
                this.add(0, "None")
            dialogChoose.setSingleChoiceItems(
                ArrayAdapter(
                    requireContext(),
                    R.layout.single_choice_item,
                    this
                ), checkItem
            ) { _, which ->
                checkItem = which
            }

            dialogChoose.setPositiveButton("Done") { dialog, _ ->
                pairTypes?.first?.apply {
                    if (checkItem - 1 > 0) {
                        viewModel.filterModel.type_id = this[checkItem - 1].id
                        typeName.text = this[checkItem - 1].name
                        if (exploreField.text.toString().isNotBlank()) {
                            updateListFromInput()
                        }
                    }
                }
                dialog.dismiss()
            }
            val dialog = dialogChoose.create()
            dialog.show()
        }
    }

    private fun FragmentSearchBinding.updateListFromInput() {
        exploreField.text.trim().let {
            if (it.isNotEmpty()) {
                viewModel.filterModel.search = it.toString()
                filter()
            }
        }
    }

    private fun FragmentSearchBinding.filter() {
        schoolsList.scrollToPosition(0)
        lifecycleScope.launch {
            schoolPagingAdapter.submitData(PagingData.empty())
        }
        requireContext().hiddenKeyboard(exploreField)
        collectFilterSchoolPagingData()
    }

    private fun FilterBottomSheetBinding.bindBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog)
        bottomSheetDialog.setContentView(this.root)
        val view = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(view!!)
        val displayMetrics = DisplayMetrics()
        bottomSheetDialog.setCancelable(false)
        requireActivity().windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val size = displayMetrics.heightPixels * 90 / 100
        behavior.setPeekHeight(size, true)
        behavior.isDraggable = false
        bottomSheetDialog.show()
        bindFillLists()
        bindClicks(dialog = bottomSheetDialog)
        bindEvaluationFees()
    }

    private fun FilterBottomSheetBinding.bindFillLists() {
        pairGrades?.second?.apply {
            educationLevel.listViewChoice.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)
            educationLevel.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                educationLevel.itemSelect = this[position]
                viewModel.filterModel.grade_id = pairGrades?.first!![position].id

            }
        }

        pairProgram?.second?.apply {
            curriculumType.listViewChoice.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

            curriculumType.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                curriculumType.itemSelect = this[position]
                viewModel.filterModel.program_id = pairProgram?.first!![position].id
            }
        }

        pairCities?.second?.apply {
            cities.listViewChoice.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

            cities.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                cities.itemSelect = this[position]
                viewModel.filterModel.city_id = pairCities?.first!![position].id
            }
        }

        requireContext().resources.getStringArray(R.array.schoolType).apply {
            typeSchool.listViewChoice.adapter =
                ArrayAdapter(requireContext(), R.layout.single_choice_item2, this)

            typeSchool.listViewChoice.setOnItemClickListener { _, _, position, _ ->
                typeSchool.itemSelect = this[position]
                viewModel.filterModel.school_type = this[position].toLowerCase(Locale.ENGLISH)
            }
        }
    }

    private fun FilterBottomSheetBinding.bindEvaluationFees() {
        evaluationValue.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            viewModel.filterModel.review = rating.toInt()
        }


        schoolFees.sliderFees.addOnChangeListener { rangeSlider, _, _ ->
            val valueFrom = rangeSlider.values[0].toInt()
            val valueTo = rangeSlider.values[1].toInt()
            schoolFees.fromValue.setText("$valueFrom")
            schoolFees.toValue.setText("$valueTo")
            viewModel.filterModel.from_price = valueFrom
            viewModel.filterModel.to_price = valueTo
        }
    }

    private fun FilterBottomSheetBinding.bindClicks(dialog: BottomSheetDialog) {
        cancelBtn.setOnClickListener {
            dialog.cancel()
        }

        clearAllBtn.setOnClickListener {

        }

        applyFilter.setOnClickListener {
            binding.filter()
            dialog.cancel()
        }
    }

    private fun collectAllRecommendedPagingData() {
        lifecycleScope.launchWhenCreated {
            viewModel.loadAllRecommended().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectAllFeaturedPagingData() {
        lifecycleScope.launchWhenCreated {
            viewModel.loadAllFeatured().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectTypedSchoolPagingData(typeId: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.loadTypedSchoolById(typeId).collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectFilterSchoolPagingData() {
        lifecycleScope.launchWhenCreated {
            viewModel.loadSchoolFilters().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectSchoolTypeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.schoolTypeStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name)
                }
                pairTypes = Pair(it, list)
            }))
        }
    }

    private fun collectSchoolProgramData() {
        lifecycleScope.launchWhenCreated {
            viewModel.schoolProgramStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name)
                }
                pairProgram = Pair(it, list)

            }))
        }
    }

    private fun collectSchoolGradesData() {
        lifecycleScope.launchWhenCreated {
            viewModel.schoolGradesStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name!!)
                }
                pairGrades = Pair(it, list)

            }))
        }
    }

    private fun collectCitiesData() {
        lifecycleScope.launchWhenCreated {
            viewModel.citiesStateFlow.collect(
                FlowEvent(onError = {},
                    onSuccess = {
                        val list: ArrayList<String> = ArrayList()
                        it.forEach { city ->
                            list.add(city.name)
                        }
                        pairCities = Pair(it, list)

                    })
            )
        }
    }

}