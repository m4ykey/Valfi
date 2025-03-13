package com.lyrics.data.domain.usecase

import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.data.domain.repository.LyricsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LyricsUseCase @Inject constructor(
    private val repository : LyricsRepository
) {

    suspend fun getTrackById(id : String) : Flow<Track> = repository.getTrackById(id)

    suspend fun searchLyrics(name : String, artist : String) : Flow<LyricsItem> = repository.searchLyrics(name, artist)

}