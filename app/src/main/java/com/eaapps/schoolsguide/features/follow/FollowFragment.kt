package com.eaapps.schoolsguide.features.follow

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
import com.eaapps.schoolsguide.databinding.FragmentFollowBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.adapter.PagingStateLoading
import com.eaapps.schoolsguide.utils.filterError
import com.eaapps.schoolsguide.utils.handleApiError
import com.eaapps.schoolsguide.utils.shortLink
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
@AndroidEntryPoint
class FollowFragment : Fragment(R.layout.fragment_follow) {

    private val binding: FragmentFollowBinding by viewBinding(FragmentFollowBinding::bind)

    private val viewModel: FollowViewModel by viewModels()

    private val followAdapter = FollowAdapter { it ->
        shortLink(it) {
            ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setChooserTitle("Share School")
                .setText(it)
                .startChooser()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindState()
        binding.bindList()
        collectFollowPagingData()
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentFollowBinding.bindList() {
        lifecycleScope.launchWhenCreated {
            followAdapter.loadStateFlow.collect {
                val isListEmpty =
                    it.refresh is LoadState.NotLoading && followAdapter.itemCount == 0
                // show empty list Text
                // only show the list if refresh succeeds
                rcFollow.visibleOrGone(!isListEmpty)

                loadRetry.apply {
                    // Show loading spinner during initial load or refresh.
                    progressBar.visibleOrGone(it.source.refresh is LoadState.Loading)

                }

                noItem.apply {
                    groupNo.visibleOrGone(isListEmpty)
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                    titleNo = getString(R.string.follow_no_msg)
                }

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = it.source.append as? LoadState.Error
                    ?: it.source.prepend as? LoadState.Error
                    ?: it.append as? LoadState.Error
                    ?: it.prepend as? LoadState.Error
                errorState?.apply {
                    handleApiError(filterError(this.error)) {
                        followAdapter.retry()
                    }
                }
            }
        }
    }


    private fun FragmentFollowBinding.bindState() {
        rcFollow.adapter = followAdapter.withLoadStateHeaderAndFooter(
            header = PagingStateLoading { followAdapter.retry() },
            footer = PagingStateLoading { followAdapter.retry() }
        )
    }

    private fun collectFollowPagingData() {
        lifecycleScope.launchWhenCreated {
            viewModel.loadData().collect {
                followAdapter.submitData(it)
            }
        }
    }

}
