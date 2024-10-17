package com.m4ykey.authentication.interceptor.token

interface TokenProvider {
    suspend fun getAccessToken() : String?
}

interface TokenHeaderProvider {
    fun getAuthorizationToken() : String?
}