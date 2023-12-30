package com.m4ykey.valfi2.module

import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.api.AuthApi
import com.m4ykey.core.Constants
import com.m4ykey.core.network.createApi
import com.squareup.moshi.Moshi

fun provideAuth(moshi: Moshi) : AuthApi {
    return createApi(Constants.SPOTIFY_AUTH_URL, moshi, AuthApi::class.java)
}

fun provideAlbum(moshi: Moshi) : AlbumApi {
    return createApi(Constants.SPOTIFY_BASE_URL, moshi, AlbumApi::class.java)
}