package com.m4ykey.data.remote.interceptor.token

import com.m4ykey.data.remote.api.AuthApi

suspend fun <T : Any> fetchAccessToken(clientId : String, clientSecret : String, api : T) : String {
    val token = generateToken(clientId, clientSecret)

    try {
        val response = (api as AuthApi).getAccessToken(token = token)
        return response.access_token ?: throw RuntimeException("Failed to fetch access token")
    } catch (e : Exception) {
        throw RuntimeException("Error", e)
    }
}