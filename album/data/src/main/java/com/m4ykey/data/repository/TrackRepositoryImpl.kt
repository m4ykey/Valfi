package com.m4ykey.data.repository

import androidx.paging.PagingData
import com.m4ykey.core.paging.createPager
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.remote.api.AlbumApi
import com.m4ykey.data.remote.interceptor.SpotifyTokenProvider
import com.m4ykey.data.remote.paging.TrackListPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api : AlbumApi,
    private val token : SpotifyTokenProvider
) : TrackRepository {
    override suspend fun getAlbumTracks(id: String): Flow<PagingData<TrackItem>> = createPager {
        TrackListPagingSource(api = api, id = id, token = token)
    }
}