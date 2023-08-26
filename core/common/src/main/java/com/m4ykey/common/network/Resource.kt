package com.m4ykey.common.network

sealed class Resource<T>(
    val data : T? = null,
    val message : String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Failure<T>(message: String) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
}