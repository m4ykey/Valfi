package com.lyrics.data.domain.repository

import com.lyrics.data.domain.model.Lyrics
import com.lyrics.data.remote.model.LyricsDto
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun searchLyrics(
        query : String,
        trackName : String,
        artistName : String,
        albumName : String
    ) : Flow<Lyrics>

}