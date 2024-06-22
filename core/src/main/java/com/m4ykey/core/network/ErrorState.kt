package com.m4ykey.core.network

sealed class ErrorState {
    data object NoError : ErrorState()
    data class Error(val message : String?) : ErrorState()
}