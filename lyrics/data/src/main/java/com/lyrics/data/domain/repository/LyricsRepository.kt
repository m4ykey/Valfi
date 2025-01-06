package com.lyrics.data.domain.repository

import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LyricsRepository {

    suspend fun searchLyrics(trackName : String, artistName : String) : Flow<LyricsItem>
    suspend fun getTrackById(id : String) : Flow<Track>

}