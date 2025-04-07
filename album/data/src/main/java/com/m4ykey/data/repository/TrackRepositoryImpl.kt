package com.m4ykey.data.repository

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.local.dao.TrackDao
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.TrackApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api : TrackApi,
    private val tokenProvider : SpotifyTokenProvider,
    private val dao : TrackDao
) : TrackRepository {
    override suspend fun getAlbumTracks(id : String, offset : Int, limit : Int): Flow<List<TrackItem>> = flow {
        val result = safeApiCall {
            api.getAlbumTracks(
                token = getToken(tokenProvider),
                id = id,
                offset = offset,
                limit = limit
            )
        }

        val tracks = result.fold(
            onSuccess = { it.items.map { item -> item.toTrackItem() } },
            onFailure = { emptyList() }
        )
        emit(tracks)
    }.flowOn(Dispatchers.IO)

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