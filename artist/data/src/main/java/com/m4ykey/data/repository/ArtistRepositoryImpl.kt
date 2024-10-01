package com.m4ykey.data.repository

import com.m4ykey.core.network.interceptor.SpotifyTokenProvider
import com.m4ykey.core.network.safeApiCall
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.repository.ArtistRepository
import com.m4ykey.data.mapper.toArtist
import com.m4ykey.data.remote.api.ArtistApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

class ArtistRepositoryImpl(
    private val api : ArtistApi,
    private val tokenProvider : SpotifyTokenProvider
) : ArtistRepository {

    private val token = runBlocking { "Bearer ${tokenProvider.getAccessToken()}" }

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