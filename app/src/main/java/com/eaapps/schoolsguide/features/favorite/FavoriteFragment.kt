package com.eaapps.schoolsguide.features.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentFavoriteBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding: FragmentFavoriteBinding by viewBinding(FragmentFavoriteBinding::bind)

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter = FavoriteAdapter({ it ->
        shortLink(it) {
            ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setChooserTitle("Share School")
                .setText(it)
                .startChooser()
        }
    }) { id ->
        viewModel.toggleFavorite(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindState(favoriteAdapter)
        binding.bindList(favoriteAdapter)
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
                    handleApiError(filterError(this.error)) {
                        favoriteAdapter.retry()
                    }
                }
            }
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
                if (favoriteAdapter.itemCount > 0) {
                    favoriteAdapter.refresh()
                    noItem.groupNo.visibleOrGone(favoriteAdapter.itemCount == 0)
                }

            }))
        }
    }
}
