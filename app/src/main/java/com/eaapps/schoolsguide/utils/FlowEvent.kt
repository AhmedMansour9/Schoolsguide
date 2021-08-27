package com.eaapps.schoolsguide.utils

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlowEvent<T : Any>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit,
    private inline val onNothing: (() -> Unit)? = null,
    private inline val onException: ((String) -> Unit)? = null

) : FlowCollector<Resource<T>> {
    override suspend fun emit(value: Resource<T>) {
        when (value) {
            is Resource.Success -> {
                onSuccess(value.data!!)

            }

            is Resource.Error -> {
                onError?.let { it(value.errorMessage) }
            }

            is Resource.Loading -> {
                onLoading?.let { it() }
            }
            is Resource.Nothing -> {
                onNothing?.let {
                    it()
                }
            }
            is Resource.Exception -> {
                onException?.let {
                    it(value.error.message!!)
                }
            }

        }
    }
}

class StateFlows<T : Any>(private val viewModelScoped: CoroutineScope) {
    private val _stateFlowMutable: MutableStateFlow<Resource<T>> =
        MutableStateFlow(Resource.Nothing())
    val stateFlow: StateFlow<Resource<T>> = _stateFlowMutable

    fun setValue(t: Resource<T>) {
        viewModelScoped.launch {
            _stateFlowMutable.emit(t)
        }
    }
}

class ObserveEvent<T : Any>(
    private inline val onError: ((String) -> Unit)? = null,
    private inline val onLoading: (() -> Unit)? = null,
    private inline val onSuccess: (T) -> Unit,
    private inline val onNothing: (() -> Unit)? = null,
    private inline val onException: ((String) -> Unit)? = null
) : Observer<Resource<T>> {
    override fun onChanged(value: Resource<T>) {
        when (value) {
            is Resource.Success -> {
                onSuccess(value.data!!)
            }

            is Resource.Error -> {
                onError?.let { it(value.errorMessage) }

            }

            is Resource.Loading -> {
                onLoading?.let { it() }
            }

            is Resource.Nothing -> {
                onNothing?.let {
                    it()
                }
            }
            is Resource.Exception -> {
                onException?.let {
                    it(value.error.message!!)
                }
            }
        }

    }
}