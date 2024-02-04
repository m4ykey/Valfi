package com.m4ykey.data.remote.interceptor.token

import android.util.Base64
import com.m4ykey.data.remote.api.AuthApi

suspend fun <T : Any> fetchAccessToken(clientId : String, clientSecret : String, api : T) : String {
    val token = "Basic " + Base64.encodeToString(
        "$clientId:$clientSecret".toByteArray(),
        Base64.NO_WRAP
    )

    try {
        when (api) {
            is AuthApi -> {
                val response = api.getAccessToken(
                    token = token
                )

                if (response.accessToken != null) {
                    return response.accessToken
                } else {
                    throw RuntimeException("Failed to fetch access token")
                }
            }
            else -> throw IllegalArgumentException("Unsupported API type")
        }
    } catch (e : Exception) {
        throw RuntimeException("Error", e)
    }
}