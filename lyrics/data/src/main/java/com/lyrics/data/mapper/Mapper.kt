package com.lyrics.data.mapper

import com.lyrics.data.domain.model.Lyrics
import com.lyrics.data.remote.model.LyricsDto
import com.lyrics.data.remote.model.LyricsDtoItem

fun LyricsDtoItem.toLyrics() : Lyrics {
    return Lyrics(
        id = id,
        name = name,
        artistName = artistName,
        albumName = albumName,
        duration = duration,
        instrumental = instrumental,
        plainLyrics = plainLyrics,
        syncedLyrics = syncedLyrics,
        trackName = trackName
    )
}