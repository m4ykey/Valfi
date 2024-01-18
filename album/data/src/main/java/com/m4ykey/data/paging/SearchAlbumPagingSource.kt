package com.m4ykey.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.interceptor.SpotifyInterceptor
import com.m4ykey.data.mapper.toAlbumItem
import com.m4ykey.data.remote.api.AlbumApi
import javax.inject.Inject

class SearchAlbumPagingSource @Inject constructor(
    private val query : String,
    private val api : AlbumApi,
    private val interceptor : SpotifyInterceptor
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
            Log.i("AccessToken", "load: $accessToken")

            val response = api.searchAlbums(
                query = query,
                limit = limit,
                offset = page * limit,
                token = accessToken
            ).albums

            val prevKey = if (response.previous.isNullOrEmpty()) page - 1 else null
            val nextKey = if (response.next.isNullOrEmpty()) null else page + 1

            LoadResult.Page(
                data = response.items.map { it.toAlbumItem() },
                nextKey = nextKey,
                prevKey = prevKey
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}