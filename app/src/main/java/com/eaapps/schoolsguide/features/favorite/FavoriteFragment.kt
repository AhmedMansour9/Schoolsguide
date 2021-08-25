package com.eaapps.schoolsguide.features.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.FragmentFavoriteBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding: FragmentFavoriteBinding by viewBinding(FragmentFavoriteBinding::bind)

    private val viewModel: FavoriteViewModel by viewModels()

    private var currentPosition: Int = -1
    private var currentList: ArrayList<SchoolResponse.SchoolData.DataSchool>? = null
    private val favoriteAdapter = FavoriteAdapter { id, position, list ->
        currentPosition = position
        currentList = ArrayList(list)
        viewModel.toggleFavorite(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindState(favoriteAdapter)
        binding.bindList(favoriteAdapter)
        binding.bindClicks()
        binding.bindToggleFavoriteResultData()
        collectFavoritePagingData()
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentFavoriteBinding.bindList(favoriteAdapter: FavoriteAdapter) {
        lifecycleScope.launchWhenCreated {
            favoriteAdapter.loadStateFlow.collect {
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && favoriteAdapter.itemCount == 0
                // show empty list Text
                // only show the list if refresh succeeds
                rcFavorite.visibleOrGone(!isListEmpty)

                loadRetry.apply {
                    // Show loading spinner during initial load or refresh.
                    progressBar.visibleOrGone(it.source.refresh is LoadState.Loading)
                    // Show the retry state if initial load or refresh fails.
                    retryButton.visibleOrGone(it.source.refresh is LoadState.Error)

                    msgError.visibleOrGone(it.source.refresh is LoadState.Error)

                }

                noItem.apply {
                    groupNo.visibleOrGone(isListEmpty)
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_favorite)
                    titleNo = getString(R.string.favorite_no_msg)
                }


                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                errorState?.apply {
                    loadRetry.msgError.text = "\uD83D\uDE28 Wooops $error"
                }
            }
        }
    }

    private fun FragmentFavoriteBinding.bindClicks() {
        loadRetry.retryButton.setOnClickListener {
            favoriteAdapter.retry()
        }
    }

    private fun FragmentFavoriteBinding.bindState(favoriteAdapter: FavoriteAdapter) {
        rcFavorite.adapter = favoriteAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateLoading { favoriteAdapter.retry() },
            footer = PagingStateLoading { favoriteAdapter.retry() }
        )
    }

    private fun collectFavoritePagingData() {
        lifecycleScope.launchWhenCreated {
            viewModel.loadData().collect {
                favoriteAdapter.submitData(it)
            }
        }
    }

    private fun FragmentFavoriteBinding.bindToggleFavoriteResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.toggleFavoriteFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                if (currentPosition > -1 && favoriteAdapter.itemCount > 0) {
                    lifecycleScope.launch {
                        currentList?.let {
                            it.removeAt(currentPosition)
                            favoriteAdapter.submitData(PagingData.from(it))
                            noItem.groupNo.visibleOrGone(it.size == 0)
                            currentPosition = -1
                        }
                    }
                }

            }))
        }
    }
}
