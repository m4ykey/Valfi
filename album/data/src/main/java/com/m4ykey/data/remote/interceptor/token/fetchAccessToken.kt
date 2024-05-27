package com.m4ykey.data.remote.interceptor.token

import com.m4ykey.data.remote.api.AuthApi

suspend fun <T : AuthApi> fetchAccessToken(clientId : String, clientSecret : String, api : T) : String {
    val token = generateToken(clientId, clientSecret)

    return try {
        val response = api.getAccessToken(token = token)
        response.access_token ?: throw RuntimeException("Failed to fetch access token")
    } catch (e : Exception) {
        throw RuntimeException("Error", e)
    }
}