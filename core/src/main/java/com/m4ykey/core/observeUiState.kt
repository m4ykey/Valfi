package com.m4ykey.core

import android.content.Context
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.m4ykey.core.network.UiState
import com.m4ykey.core.views.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> observeUiState(
    flow : Flow<UiState<T>>,
    onSuccess : (T) -> Unit,
    lifecycleScope : CoroutineScope,
    progressBar : CircularProgressIndicator,
    context : Context,
    message : String? = null
) {
    lifecycleScope.launch {
        flow.collect { uiState ->
            when (uiState) {
                is UiState.Error -> {
                    progressBar.isVisible = false
                    val errorMessage = message ?: uiState.exception.message ?: context.getString(R.string.generic_error)
                    showToast(context, errorMessage)
                }
                is UiState.Success -> {
                    progressBar.isVisible = false
                    uiState.data?.let(onSuccess)
                }
                is UiState.Loading -> {
                    progressBar.isVisible = true
                }
            }
        }
    }
}