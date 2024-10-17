package com.m4ykey.data.repository

import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.domain.repository.TrackRepository
import com.m4ykey.data.mapper.toTrackItem
import com.m4ykey.data.remote.api.TrackApi
import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val api : TrackApi,
    private val token : com.m4ykey.authentication.interceptor.SpotifyTokenProvider
) : TrackRepository {
    override suspend fun getAlbumTracks(id : String, offset : Int, limit : Int): Flow<List<TrackItem>> = flow {
        try {
            val result = api.getAlbumTracks(
                token = "Bearer ${token.getAccessToken()}",
                offset = offset,
                limit = limit,
                id = id
            )
            val trackResult = result.items.map { it.toTrackItem() }
            emit(trackResult)
        } catch (e : Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}