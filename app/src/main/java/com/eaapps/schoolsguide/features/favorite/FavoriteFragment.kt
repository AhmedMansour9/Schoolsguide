package com.eaapps.schoolsguide.features.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentFavoriteBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding: FragmentFavoriteBinding by viewBinding(FragmentFavoriteBinding::bind)

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter = FavoriteAdapter{
        viewModel.toggleFavorite(it)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindState(favoriteAdapter)
        binding.bindList(favoriteAdapter)
        collectFavoritePagingData()
    }

    private fun FragmentFavoriteBinding.bindList(favoriteAdapter: FavoriteAdapter) {
        lifecycleScope.launchWhenCreated {
            favoriteAdapter.loadStateFlow.collect {
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && favoriteAdapter.itemCount == 0
                // show empty list Text
                // only show the list if refresh succeeds
                rcFavorite.visibleOrGone(!isListEmpty)
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
}
