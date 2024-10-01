package com.m4ykey.core.network.interceptor.token

interface TokenProvider {
    suspend fun getAccessToken() : String?
}

interface TokenHeaderProvider {
    fun getAuthorizationToken() : String?
}