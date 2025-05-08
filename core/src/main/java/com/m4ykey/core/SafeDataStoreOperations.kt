package com.m4ykey.core

import java.io.IOException

suspend fun <T> safeDataStoreOperations(operation : suspend () -> T) : T? {
    return runCatching {
        operation()
    }.onFailure { exception ->
        if (exception is IOException) {
            exception.printStackTrace()
        }
    }.getOrNull()
}