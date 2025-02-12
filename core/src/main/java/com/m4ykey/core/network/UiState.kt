package com.m4ykey.core.network

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data : T) : UiState<T>()
    data class Error(val exception : Throwable) : UiState<Nothing>()
}