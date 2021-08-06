package com.eaapps.schoolsguide.utils

import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.FlowCollector

class FlowEvent<T : Any>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit,
    private inline val onNothing: (() -> Unit)? = null

) : FlowCollector<Resource<T>> {
    override suspend fun emit(value: Resource<T>) {
        when (value) {
            is Resource.Success -> {
                onSuccess(value.data!!)

            }

            is Resource.Error -> {
                onError?.let { it(value.errorMessage!!) }
            }

            is Resource.Loading -> {
                onLoading?.let { it() }
            }
            is Resource.Nothing -> {
                onNothing?.let {
                    it()
                }
            }
        }
    }
}

class ObserveEvent<T : Any>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit
) : Observer<Resource<T>> {
    override fun onChanged(value: Resource<T>?) {
        when (value) {
            is Resource.Success<*> -> {
                value.let {
                    onSuccess
                }
            }

            is Resource.Error<*> -> {
                onError?.let { it(value.errorMessage) }
            }

            is Resource.Loading<*> -> {
                onLoading?.let { it() }
            }
        }

    }
}