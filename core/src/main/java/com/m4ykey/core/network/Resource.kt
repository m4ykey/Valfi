package com.m4ykey.core.network

sealed class Resource<out T> {
    data class Success<out T>(val data : T) : Resource<T>()
    data class Error(val message : String, val data : Any? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}