package com.m4ykey.core.network

sealed class NetworkError(message : String? = null, cause : Throwable? = null) : Throwable(message, cause) {
    class HttpError(message : String? = null, cause : Throwable? = null) : NetworkError(message, cause)
    class NoInternetConnection(message : String? = null, cause : Throwable? = null) : NetworkError(message, cause)
}