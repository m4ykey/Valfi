package com.m4ykey.core.views

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel : ViewModel() {

    val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> get() = _error

    var isPaginationEnded = false

}