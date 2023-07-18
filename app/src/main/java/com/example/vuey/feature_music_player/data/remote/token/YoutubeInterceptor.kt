package com.example.vuey.feature_music_player.data.remote.token

import com.example.vuey.BuildConfig.YOUTUBE_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class YoutubeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("key", YOUTUBE_API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}