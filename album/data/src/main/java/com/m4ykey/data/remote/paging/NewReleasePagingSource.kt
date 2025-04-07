package com.m4ykey.data.remote.paging

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi

class NewReleasePagingSource(
    private val api : AlbumApi,
    private val tokenProvider : SpotifyTokenProvider
) : BasePagingSource<AlbumItem>() {

    override suspend fun loadData(offset: Int, limit: Int): Result<List<AlbumItem>> {
        return safeApiCall {
            api.getNewReleases(
                token = getToken(tokenProvider),
                offset = offset,
                limit = limit
            )
        }.map { it.albums.items?.map { album -> album.toAlbumItem() }!! }
    }
}