package com.m4ykey.core

import java.io.IOException

suspend fun <T> safeDataStoreOperations(operation : suspend () -> T) : T? {
    return try {
        operation()
    } catch (e : IOException) {
        e.printStackTrace()
        null
    }
}