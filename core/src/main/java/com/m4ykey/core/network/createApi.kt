package com.m4ykey.core.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun <T> createApi(baseUrl : String, moshi : Moshi, apiClass : Class<T>) : T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(apiClass)
}