package com.m4ykey.data.repository

import com.m4ykey.data.domain.repository.LyricsRepository
import com.m4ykey.data.remote.api.LyricsApi
import javax.inject.Inject

class LyricsRepositoryImpl @Inject constructor(
    private val lyricsApi: LyricsApi
) : LyricsRepository {

    override suspend fun searchLyrics(artist: String, song: String): String {

    }

    override suspend fun getSongLyrics(songUrl: String): String {

    }
}