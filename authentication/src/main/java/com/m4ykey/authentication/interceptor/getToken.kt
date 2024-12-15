package com.m4ykey.authentication.interceptor

suspend fun getToken(tokenProvider: SpotifyTokenProvider) : String {
    return "Bearer ${tokenProvider.getAccessToken()}"
}