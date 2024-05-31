package com.m4ykey.data.remote.paging

import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import javax.inject.Inject

class SearchAlbumPagingSource @Inject constructor(
    private val query : String,
    override val api : AlbumApi,
    private val token : SpotifyTokenProvider
) : BasePagingSource<AlbumItem>(api) {

    override suspend fun loadPage(params: LoadParams<Int>, page: Int, limit: Int): List<AlbumItem> {
        val response = api.searchAlbums(
            query = query,
            limit = limit,
            offset = page * params.loadSize,
            token = "Bearer ${token.getAccessToken()}"
        ).albums

        return response.items?.map { it.toAlbumItem() }.orEmpty()
    }
}