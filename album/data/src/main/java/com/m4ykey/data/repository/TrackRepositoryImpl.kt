package com.m4ykey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.TrackApi
import com.m4ykey.data.remote.paging.TrackPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api : TrackApi,
    private val tokenProvider : SpotifyTokenProvider,
    private val dao : TrackDao,
    private val dispatcherIO : CoroutineDispatcher
) : TrackRepository {
    override suspend fun getAlbumTracks(id : String, offset : Int, limit : Int): Flow<PagingData<TrackItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                TrackPagingSource(
                    api = api,
                    id = id,
                    tokenProvider = tokenProvider
                )
            }
        ).flow.flowOn(dispatcherIO)
    }

    override suspend fun insertTracks(track: List<TrackEntity>) {
        return dao.insertTrack(track)
    }

    override fun getTracksById(albumId: String): List<TrackEntity> {
        return dao.getTracksById(albumId)
    }

    override suspend fun deleteTracksById(albumId: String) {
        return dao.deleteTracksById(albumId)
    }
}