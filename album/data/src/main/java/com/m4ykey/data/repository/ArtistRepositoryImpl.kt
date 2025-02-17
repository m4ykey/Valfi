package com.m4ykey.data.repository

import com.m4ykey.authentication.interceptor.SpotifyTokenProvider
import com.m4ykey.authentication.interceptor.getToken
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.repository.ArtistRepository
import com.m4ykey.data.mapper.toArtist
import com.m4ykey.data.remote.api.ArtistApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val api : ArtistApi,
    private val tokenProvider: SpotifyTokenProvider
) : ArtistRepository {

    override suspend fun getSeveralArtists(ids: String): Flow<List<Artist>> = flow {
        try {
            val result = api.getSeveralArtists(
                token = getToken(tokenProvider),
                ids = ids
            )
            val artistResult = result.map { it.toArtist() }
            emit(artistResult)
        } catch (e : Exception) {
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}