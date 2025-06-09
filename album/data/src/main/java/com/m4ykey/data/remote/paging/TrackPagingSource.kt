package com.m4ykey.data.remote.paging

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.core.paging.BasePagingSource
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.TrackApi

class TrackPagingSource(
    private val api : TrackApi,
    private val tokenProvider : SpotifyTokenProvider,
    private val id : String
) : BasePagingSource<TrackItem>() {

    override suspend fun loadData(
        offset: Int,
        limit: Int
    ): Result<List<TrackItem>> {
        return safeApiCall {
            api.getAlbumTracks(
                token = getToken(tokenProvider),
                id = id,
                limit = limit,
                offset = offset
            )
        }.map { it.items.map { track -> track.toTrackItem() } }
    }
}