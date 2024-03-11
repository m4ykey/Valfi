package com.m4ykey.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import javax.inject.Inject

class NewReleasePagingSource @Inject constructor(
    private val api : AlbumApi,
    private val token : SpotifyTokenProvider
) : PagingSource<Int, AlbumItem>() {

    override fun getRefreshKey(state: PagingState<Int, AlbumItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.nextKey ?: closestPage.prevKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumItem> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize.coerceIn(1, 20)

            val response = api.getNewReleases(
                limit = limit,
                token = "Bearer ${token.getAccessToken()}",
                offset = page * params.loadSize
            ).albums

            LoadResult.Page(
                data = response.items.map { it.toAlbumItem() },
                nextKey = if (response.next.isNullOrEmpty()) null else page + 1,
                prevKey = if (page > 0) page - 1 else null
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}