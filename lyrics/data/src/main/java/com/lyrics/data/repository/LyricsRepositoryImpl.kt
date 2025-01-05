package com.lyrics.data.repository

import android.util.Log
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.repository.LyricsRepository
import com.lyrics.data.mapper.toLyrics
import com.lyrics.data.remote.api.LyricsApi
import com.m4ykey.core.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LyricsRepositoryImpl @Inject constructor(
    private val api: LyricsApi
) : LyricsRepository {

    override suspend fun searchLyrics(trackName: String, artistName: String): Flow<LyricsItem> = flow {
        val result = safeApiCall {
            val response = api.searchLyrics(
                trackName = trackName,
                artistName = artistName
            )
            val lyricsList = response.map { it.toLyrics() }
            lyricsList.firstOrNull() ?: throw Exception("Lyrics not found")
        }

        result.getOrNull()?.let { lyrics ->
            emit(lyrics)
        } ?: throw Exception("Lyrics not found")

    }.flowOn(Dispatchers.IO).catch { e ->
        Log.i("LyricsRepository", "Error: ${e.message}", e)
    }
}