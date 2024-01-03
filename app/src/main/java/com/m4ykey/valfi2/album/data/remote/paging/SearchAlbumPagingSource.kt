package com.m4ykey.valfi2.album.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.valfi2.album.data.domain.model.Item
import com.m4ykey.valfi2.album.data.mappers.toItem
import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.interceptor.SpotifyInterceptor

class SearchAlbumPagingSource(
    private val api: AlbumApi,
    private val interceptor : SpotifyInterceptor
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.nextKey ?: closestPage.prevKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize.coerceIn(1, 50)

            val response = api.searchAlbums(
                auth = "Bearer ${interceptor.getAccessToken()}",
                limit = limit,
                offset = page * limit
            ).albums

            val prevKey = if (page > 0) page - 1 else null
            val nextKey = if (response.next.isNullOrEmpty()) null else page + 1

            LoadResult.Page(
                data = response.items.map { it.toItem() },
                nextKey = nextKey,
                prevKey = prevKey
            )

        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}