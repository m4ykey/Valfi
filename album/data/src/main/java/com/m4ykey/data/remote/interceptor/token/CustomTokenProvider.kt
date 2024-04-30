package com.m4ykey.data.remote.interceptor.token

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CustomTokenProvider @Inject constructor(
    private val tokenHeaderProvider: TokenHeaderProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenHeaderProvider.getAuthorizationToken()

        val newRequest = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", accessToken ?: "")
            .build()
        return chain.proceed(newRequest)
    }
}