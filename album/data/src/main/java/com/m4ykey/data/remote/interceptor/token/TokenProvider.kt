package com.m4ykey.data.remote.interceptor.token

interface TokenProvider {
    suspend fun getAccessToken() : String
}

interface TokenHeaderProvider {
    fun getAuthorizationToken() : String?
}