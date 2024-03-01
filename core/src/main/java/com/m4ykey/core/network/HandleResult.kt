package com.m4ykey.core.network

fun <T> Resource<T>.handleResult(onSuccess: (T) -> Unit, onError : (Exception) -> Unit) {
    when (this) {
        is Resource.Success -> onSuccess(data!!)
        is Resource.Error -> onError(Exception(message ?: "Unknown error"))
        else -> {}
    }
}