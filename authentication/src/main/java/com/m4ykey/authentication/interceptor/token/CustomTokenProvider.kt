package com.m4ykey.authentication.interceptor.token

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CustomTokenProvider @Inject constructor(
    private val tokenHeaderProvider: TokenHeaderProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenHeaderProvider.getAuthorizationToken() ?: throw RuntimeException("Failed to get access token")

        val newRequest = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}