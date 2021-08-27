package com.eaapps.schoolsguide.features.search

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.TypeResponse
import com.eaapps.schoolsguide.databinding.FragmentSearchBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.features.favorite.PagingStateLoading
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "SearchFragment"

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private val shareViewModel: ShareViewModel by activityViewModels()
    private var searchType: SearchType? = null
    private var listMode = true
    private var checkItem = 0
    private var positionFavorite = -1
    private var pairTypes: Pair<List<TypeResponse.TypeData>, ArrayList<String>>? = null

    private val schoolPagingAdapter = SchoolPagingAdapter(onToggleFavorite = { position, id ->
        positionFavorite = position
        viewModel.toggleFavorite(id)
    }) {
        launchFragment(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(
            R.style.AppTheme,
            Color.WHITE,
            true,
            shouldInterceptBackPress = true
        ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArg()
        collectSchoolTypeData()
        toggleFavoriteResultData()
        binding.bindListChangeMode(listMode)
        binding.bindState(schoolPagingAdapter)
        binding.bindList(schoolPagingAdapter)
        binding.bindSearch()
        binding.bindClicks()
        binding.bindCollectFilterFire()
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

                noItem.apply {
                    groupNo.visibleOrGone(isListEmpty && (!shareViewModel.filterModel.search.isNullOrBlank() || viewModel.filterModel.isFilter()))
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_search)
                    titleNo = getString(R.string.search_no_msg)
                }

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
            bindDialogTypeSchool()
        }

        filterBtn.setOnClickListener {
            launchFragment(SearchFragmentDirections.actionSearchFragmentToFilterBottomFragment())
        }

        backBtn.setOnClickListener {
            dismiss()
        }

        mapSearchBtn.setOnClickListener {
            launchFragment(SearchFragmentDirections.actionSearchFragmentToSearchMapDialogFragment())
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
                    if (checkItem > 0) {
                        shareViewModel.filterModel.type_id = this[checkItem - 1].id
                        typeName.text = this[checkItem - 1].name
                    } else {
                        shareViewModel.filterModel.type_id = null
                        typeName.text = getString(R.string.school_type)
                    }
                    if (exploreField.text.toString().isNotBlank()) {
                        updateListFromInput()
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
                shareViewModel.filterModel.search = it.toString()
                filter()
            }
        }
    }

    private fun FragmentSearchBinding.emptyList() {
        schoolsList.scrollToPosition(0)
        lifecycleScope.launch {
            schoolPagingAdapter.submitData(PagingData.empty())
        }
    }

    private fun FragmentSearchBinding.filter() {
        emptyList()
        requireContext().hiddenKeyboard(exploreField)
        collectFilterSchoolPagingData()
    }

    private fun FragmentSearchBinding.bindCollectFilterFire() {
        lifecycleScope.launchWhenCreated {
            shareViewModel.filterFire.collect(FlowEvent(onSuccess = {
                when (it) {
                    Filter.APPLY_FILTER -> {
                        filter()
                        if (shareViewModel.filterModel.isFilter()) {
                            filterBtn.setImageResource(R.drawable.baseline_filter_alt_black_48)
                        } else {
                            filterBtn.setImageResource(R.drawable.outline_filter_alt_black_48)
                        }
                    }
                    Filter.CLEAR_FILTER -> {
                        if (shareViewModel.filterModel.isFilter()) {
                            filterBtn.setImageResource(R.drawable.baseline_filter_alt_black_48)
                        } else {
                            filterBtn.setImageResource(R.drawable.outline_filter_alt_black_48)
                        }
                    }
                }
            }, onNothing = {
                emptyList()
            }))
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
            shareViewModel.loadSchoolFilters().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectSchoolTypeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.schoolTypeStateFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                val list: ArrayList<String> = ArrayList()
                it.forEach { typeData ->
                    list.add(typeData.name)
                }
                pairTypes = Pair(it, list)
            }))
        }
    }

    private fun toggleFavoriteResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.toggleFavoriteStateFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                lifecycleScope.launch {
                    if (positionFavorite >= 0) {
                        schoolPagingAdapter.updateItem(positionFavorite)
                        positionFavorite = -1
                    }
                }
            }))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        lifecycleScope.launchWhenStarted {
            shareViewModel.filterFire.emit(Resource.Nothing())
        }
        findNavController().navigateUp()
    }

    override fun onStart() {
        super.onStart()
        shareViewModel.mapSearch = false
    }
}