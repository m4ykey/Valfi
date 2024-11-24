package com.m4ykey.core.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

open class BaseViewModel : ViewModel() {

    val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> get() = _error

    var isPaginationEnded = false

    protected fun <T> runFlow (
        flow : Flow<T>,
        resultState : MutableStateFlow<T?>
    ) {
        _isLoading.value = true
        _error.value = null

        flow.onEach { result ->
            resultState.value = result
        }.catch { e ->
            _error.value = e.message
        }.onCompletion {
            _isLoading.value = true
        }.launchIn(viewModelScope)
    }

}