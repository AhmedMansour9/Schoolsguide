package com.eaapps.schoolsguide.features.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.databinding.LoadStateFooterViewItemBinding
import com.eaapps.schoolsguide.utils.visibleOrGone

class PagingStateLoading(private val retry: () -> Unit) :
    LoadStateAdapter<PagingStateLoading.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        private val loadStateFooterViewItemBinding: LoadStateFooterViewItemBinding,
        retry: () -> Unit
    ) :
        RecyclerView.ViewHolder(loadStateFooterViewItemBinding.root) {

        init {
            loadStateFooterViewItemBinding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error)
                loadStateFooterViewItemBinding.errorMsg.text = loadState.error.localizedMessage
            loadStateFooterViewItemBinding.progressBar.visibleOrGone(loadState is LoadState.Loading)
            loadStateFooterViewItemBinding.retryButton.visibleOrGone(loadState is LoadState.Error)
            loadStateFooterViewItemBinding.errorMsg.visibleOrGone(loadState is LoadState.Error)

        }


    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder(
            LoadStateFooterViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), retry
        )
}