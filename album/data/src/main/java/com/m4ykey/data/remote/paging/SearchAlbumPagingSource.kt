package com.m4ykey.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import javax.inject.Inject

class SearchAlbumPagingSource @Inject constructor(
    private val query : String,
    private val api : AlbumApi,
    private val interceptor : SpotifyTokenProvider
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
            val limit = params.loadSize.coerceIn(1, 50)
            val accessToken = "Bearer ${interceptor.getAccessToken()}"

            val response = api.searchAlbums(
                query = query,
                limit = limit,
                offset = page * limit,
                token = accessToken
            ).albums

            val uniqueItems = response.items.distinctBy { it.id }

            val prevKey = if (page > 0) page - 1 else null
            val nextKey = if (uniqueItems.isEmpty() || response.next.isNullOrEmpty()) null else page + 1

            LoadResult.Page(
                data = uniqueItems.map { it.toAlbumItem() },
                nextKey = nextKey,
                prevKey = prevKey
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}