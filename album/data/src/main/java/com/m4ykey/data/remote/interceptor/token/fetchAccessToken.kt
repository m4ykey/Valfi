package com.m4ykey.data.remote.interceptor.token

import android.content.Context
import com.m4ykey.core.network.isInternetAvailable
import com.m4ykey.data.remote.api.AuthApi
import java.io.IOException

suspend fun <T : AuthApi> fetchAccessToken(context : Context, clientId : String, clientSecret : String, api : T) : String {
    if (!isInternetAvailable(context)) {
        throw IOException("No internet connection")
    }

    val token = generateToken(clientId, clientSecret)

    return try {
        val response = api.getAccessToken(token = token)
        response.access_token ?: throw RuntimeException("Failed to fetch access token")
    } catch (e : Exception) {
        throw RuntimeException("Error", e)
    }
}