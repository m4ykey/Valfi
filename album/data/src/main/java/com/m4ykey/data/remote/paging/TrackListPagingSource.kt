package com.m4ykey.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.database.AlbumDatabase
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import javax.inject.Inject

class TrackListPagingSource @Inject constructor(
    private val id : String,
    private val api : AlbumApi,
    private val interceptor : SpotifyTokenProvider,
    private val db : AlbumDatabase
) : PagingSource<Int, TrackItem>() {
    override fun getRefreshKey(state: PagingState<Int, TrackItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.nextKey ?: closestPage.prevKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrackItem> {
        return try {
            val page = params.key ?: 0
            val limit = params.loadSize.coerceIn(1, 20)

            val response = api.getAlbumTracks(
                id = id,
                limit = limit,
                offset = page * params.loadSize,
                token = "Bearer ${interceptor.getAccessToken()}"
            )

            LoadResult.Page(
                data = response.items.map { it.toTrackItem() },
                nextKey = if (response.next.isNullOrEmpty()) null else page + 1,
                prevKey = if (page > 0) page - 1 else null
            )
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}