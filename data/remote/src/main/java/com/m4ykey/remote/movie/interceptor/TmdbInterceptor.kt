package com.m4ykey.remote.movie.interceptor

import com.m4ykey.remote.BuildConfig.TMDB_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class TmdbInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $TMDB_API_KEY")
            .build()
        return chain.proceed(request)
    }

}