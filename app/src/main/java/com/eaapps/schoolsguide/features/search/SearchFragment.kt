package com.eaapps.schoolsguide.features.search

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentSearchBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.features.favorite.PagingStateLoading
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel: SearchViewModel by viewModels()

    private var searchType: SearchType? = null

    private var listMode = true

    private val schoolPagingAdapter = SchoolPagingAdapter {

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArg()
        binding.bindListChangeMode(listMode)
        binding.bindState(schoolPagingAdapter)
        binding.bindList(schoolPagingAdapter)
        loadListData()
        binding.modeListBtn.setOnClickListener {
            listMode = !listMode
            binding.bindListChangeMode(listMode)
        }
    }

    private fun loadListData() {
        searchType?.apply {
            when {
                this.type > -1 -> {
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
        }
        else {
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

    private fun collectTypedSchoolPagingData(typeId:Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.loadTypedSchoolById(typeId).collect {
                schoolPagingAdapter.submitData(it)
            }
        }
    }
}