package com.m4ykey.data.repository

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.top_tracks.Track
import com.m4ykey.data.domain.repository.ArtistRepository
import com.m4ykey.data.mapper.toArtist
import com.m4ykey.data.mapper.toTrack
import com.m4ykey.data.remote.api.ArtistApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val api : ArtistApi,
    private val tokenProvider : SpotifyTokenProvider
) : ArtistRepository {

    private val token = runBlocking { "Bearer ${tokenProvider.getAccessToken()}" }

    override suspend fun getArtistTopTracks(id: String): Flow<List<Track>> = flow {
        try {
            val result = api.getArtistTopTracks(token = token, id = id)
            val trackResult = result.tracks
                ?.map { it.toTrack() }
                ?.take(5)
                ?: emptyList()
            emit(trackResult)
        } catch (e : Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getArtist(id: String): Flow<Artist> = flow {
        val result = safeApiCall {
            api.getArtist(
                token = token,
                id = id
            ).toArtist()
        }
        if (result.isSuccess) {
            emit(result.getOrThrow())
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error")
        }
    }.flowOn(Dispatchers.IO)
}