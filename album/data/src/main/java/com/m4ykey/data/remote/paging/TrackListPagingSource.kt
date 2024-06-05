package com.m4ykey.data.remote.paging

import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import javax.inject.Inject

class TrackListPagingSource @Inject constructor(
    private val token : SpotifyTokenProvider,
    override val api : AlbumApi,
    private val id : String
) : BasePagingSource<TrackItem>(api) {

    override suspend fun loadPage(params: LoadParams<Int>, page: Int): List<TrackItem> {
        return api.getAlbumTracks(
            token = "Bearer ${token.getAccessToken()}",
            limit = params.loadSize.coerceIn(1, 20),
            offset = page * params.loadSize,
            id = id
        ).items.map { it.toTrackItem() }
    }
}