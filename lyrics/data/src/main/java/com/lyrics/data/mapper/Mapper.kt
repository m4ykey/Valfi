package com.lyrics.data.mapper

import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.remote.model.LyricsDtoItem

fun LyricsDtoItem.toLyrics() : LyricsItem {
    return LyricsItem(
        id = id ?: 0,
        name = name.orEmpty(),
        artistName = artistName.orEmpty(),
        duration = duration ?: 0.0,
        plainLyrics = plainLyrics.orEmpty(),
        trackName = trackName.orEmpty()
    )
}