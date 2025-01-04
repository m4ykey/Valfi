package com.lyrics.data.repository

import com.lyrics.data.domain.model.Lyrics
import com.lyrics.data.domain.repository.LyricsRepository
import com.lyrics.data.mapper.toLyrics
import com.lyrics.data.remote.api.LyricsApi
import com.m4ykey.core.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LyricsRepositoryImpl @Inject constructor(
    private val api : LyricsApi
) : LyricsRepository {

    override suspend fun searchLyrics(
        query: String,
        trackName: String,
        artistName: String,
        albumName: String
    ): Flow<Lyrics> = flow {
        val result = safeApiCall {
            api.searchLyrics(
                query = query,
                trackName = trackName,
                artistName = artistName,
                albumName = albumName
            ).map { it.toLyrics() }.first()
        }

        if (result.isSuccess) {
            emit(result.getOrThrow())
        } else {
            throw result.exceptionOrNull() ?: Exception("Unknown error")
        }
    }.flowOn(Dispatchers.IO)
}