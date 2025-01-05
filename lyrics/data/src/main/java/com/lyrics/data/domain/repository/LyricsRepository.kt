package com.lyrics.data.domain.repository

import com.lyrics.data.domain.model.LyricsItem
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun searchLyrics(trackName : String, artistName : String) : Flow<LyricsItem>

}