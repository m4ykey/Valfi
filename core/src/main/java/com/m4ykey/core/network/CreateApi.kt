package com.m4ykey.core.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

inline fun <reified T> createApi(baseUrl : String, moshi : Moshi) : T {
    require(baseUrl.isNotBlank()) { "Base Url cannot be blank" }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return retrofit.create(T::class.java)
}