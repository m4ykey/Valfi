package com.m4ykey.data.remote.interceptor.token

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CustomTokenProvider @Inject constructor(
    private val tokenHeaderProvider: TokenHeaderProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()

        val accessToken = tokenHeaderProvider.getAuthorizationToken()

        val newRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", accessToken)
            .build()
        chain.proceed(newRequest)
    }
}