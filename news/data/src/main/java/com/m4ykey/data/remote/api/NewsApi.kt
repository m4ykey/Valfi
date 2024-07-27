package com.m4ykey.data.remote.api

import com.m4ykey.core.BuildConfig.NEWS_API_KEY
import com.m4ykey.core.Constants.DOMAINS
import com.m4ykey.data.domain.NewsSort
import com.m4ykey.data.remote.model.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun getMusicNews(
        @Query("q") query : String = "music",
        @Query("pageSize") pageSize : Int,
        @Query("page") page : Int,
        @Query("sortBy") sortBy : String = NewsSort.PUBLISHED_AT.value,
        @Query("domains") domains : String = DOMAINS,
        @Query("apiKey") apiKey : String = NEWS_API_KEY
    ) : NewsDto

}