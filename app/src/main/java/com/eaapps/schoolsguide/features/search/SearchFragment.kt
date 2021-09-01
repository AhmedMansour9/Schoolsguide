package com.eaapps.schoolsguide.features.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentSearchBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.features.adapter.PagingStateLoading
import com.eaapps.schoolsguide.features.search.adapter.SchoolPagingAdapter
import com.eaapps.schoolsguide.features.search.viewmodels.Filter
import com.eaapps.schoolsguide.features.search.viewmodels.FilterViewModel
import com.eaapps.schoolsguide.features.search.viewmodels.SearchViewModel
import com.eaapps.schoolsguide.features.search.viewmodels.ShareViewModel
import com.eaapps.schoolsguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private val shareViewModel: ShareViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)
    }
    private val filterViewModel: FilterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
    }
    private var positionFavorite = -1

    private val schoolPagingAdapter = SchoolPagingAdapter(onToggleFavorite = { position, id ->
        positionFavorite = position
        viewModel.toggleFavorite(id)
    }, onShareSchool = { it ->
        shortLink(it) {
            ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setChooserTitle("Share School")
                .setText(it)
                .startChooser()
        }
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
        checkInternet()
        binding.bindListChangeMode(true)
        binding.bindState()
        binding.bindList()
        binding.bindSearch()
        binding.bindClicks()
        binding.bindCollectFilterFire()
        filterViewModel.load()
        toggleFavoriteResultData()

    }

    private fun checkInternet() {
        lifecycleScope.launchWhenStarted {
            NetworkChecker.isOnline().collect(
                FlowEvent(
                    onError = {
                        requireActivity().noInternetDialog {
                            loadCollectDataByType()
                        }
                    },
                    onSuccess = {
                        loadCollectDataByType()
                    })
            )
        }
    }

    private var searchType: SearchType? = null

    private fun setupArg() {
        SearchFragmentArgs.fromBundle(requireArguments()).searchType.apply {
            searchType = this
            loadCollectDataByType()
        }

    }

    private fun loadCollectDataByType() {
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
                this.search -> {
                    binding.filter()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentSearchBinding.bindList() {
        lifecycleScope.launchWhenCreated {
            schoolPagingAdapter.loadStateFlow.collect { it ->
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && schoolPagingAdapter.itemCount == 0
                // show empty list Text
                // only show the list if refresh succeeds
                schoolsList.visibleOrGone(!isListEmpty)
                groupListMode.visibleOrGone(!isListEmpty)
                // Show loading spinner during initial load or refresh.
                progressBar.visibleOrGone(it.source.refresh is LoadState.Loading)
                // Show the retry state if initial load or refresh fails.
                retryButton.visibleOrGone(it.source.refresh is LoadState.Error)

                noItem.apply {
                    groupNo.visibleOrGone(isListEmpty && (!shareViewModel.filterModel.search.isNullOrBlank() || viewModel.filterModel.isFilter()))
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_search)
                    titleNo = getString(R.string.search_no_msg)
                }

                if (isListEmpty)
                    schoolResult.text = getString(R.string.no_result)
                else
                    schoolResult.text =
                        "${schoolPagingAdapter.itemCount} ${getString(R.string.result_school)}"
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                errorState?.let {
                    handleApiError(filterError(it.error)) {
                        schoolPagingAdapter.retry()
                    }
                }
            }
        }
    }

    private fun FragmentSearchBinding.bindListChangeMode(listMode: Boolean) {
        if (listMode) {
            modeList.setColorFilter(Color.parseColor("#0073DB"))
            modeGrid.setColorFilter(Color.parseColor("#686868"))
            schoolPagingAdapter.changeMode(listMode)
            schoolsList.layoutManager = LinearLayoutManager(requireContext())
        } else {
            modeGrid.setColorFilter(Color.parseColor("#0073DB"))
            modeList.setColorFilter(Color.parseColor("#686868"))
            schoolPagingAdapter.changeMode(listMode)
            schoolsList.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun FragmentSearchBinding.bindState() {
        schoolsList.adapter = schoolPagingAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateLoading { schoolPagingAdapter.retry() },
            footer = PagingStateLoading { schoolPagingAdapter.retry() }
        )
    }

    private fun FragmentSearchBinding.bindSearch() {
        exploreFieldEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else
                false
        }

        exploreFieldEdit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else
                false
        }

        exploreFieldEdit.addTextChangedListener {
            it?.apply {
                val lengthText = this.length
                if (lengthText > 0) {
                    exploreField.setEndIconDrawable(R.drawable.round_clear_black_20)
                    exploreField.setEndIconOnClickListener {
                        exploreFieldEdit.setText("")
                        shareViewModel.filterModel.search = null
                        exploreFieldEdit.clearFocus()
                        if (shareViewModel.filterModel.isFilter())
                            filter()
                    }
                } else {
                    exploreField.setEndIconDrawable(R.drawable.ic_search_)
                }
            }

        }
    }

    private fun FragmentSearchBinding.bindClicks() {
        modeList.setOnClickListener {
            bindListChangeMode(true)
        }

        modeGrid.setOnClickListener {
            bindListChangeMode(false)
        }

        filterBtn.setOnClickListener {
            launchFragment(SearchFragmentDirections.actionSearchFragmentToFilterBottomFragment(0))
        }

        retryButton.setOnClickListener {
            schoolPagingAdapter.retry()
        }

        backBtn.setOnClickListener {
            dismiss()
        }

        mapSearchBtn.setOnClickListener {
            launchFragment(SearchFragmentDirections.actionSearchFragmentToSearchMapDialogFragment())
        }
    }

    private fun FragmentSearchBinding.updateListFromInput() {
        exploreFieldEdit.text.toString().trim().let {
            if (it.isNotEmpty()) {
                shareViewModel.filterModel.search = it
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
        shareViewModel.filterFire.observe(viewLifecycleOwner, ObserveEvent(onSuccess = {
            when (it) {
                Filter.APPLY_FILTER -> {
                    if (shareViewModel.filterModel.isFilter()) {
                        filter()
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
        }, onNothing = { emptyList() }))

        lifecycleScope.launchWhenCreated {
        }
    }

    private fun collectAllRecommendedPagingData() {
        lifecycleScope.launchWhenResumed {
            viewModel.loadAllRecommended().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectAllFeaturedPagingData() {
        lifecycleScope.launchWhenResumed {
            viewModel.loadAllFeatured().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectTypedSchoolPagingData(typeId: Int) {
        lifecycleScope.launchWhenResumed {
            viewModel.loadTypedSchoolById(typeId).collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun collectFilterSchoolPagingData() {
        lifecycleScope.launchWhenResumed {
            shareViewModel.loadSchoolFilters().collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }

    private fun toggleFavoriteResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.toggleFavoriteStateFlow.stateFlow.collect(FlowEvent(onError = {
                requireActivity().toastingError(it)
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
        shareViewModel.filterSearchDefault()
        findNavController().navigateUp()
    }
}