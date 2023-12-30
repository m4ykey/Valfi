package com.m4ykey.valfi2.album.data.remote.paging

import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.interceptor.SpotifyInterceptor

class SearchAlbumPagingSource(
    private val albumApi: AlbumApi,
    private val query : String,
    private val interceptor : SpotifyInterceptor
) {
}