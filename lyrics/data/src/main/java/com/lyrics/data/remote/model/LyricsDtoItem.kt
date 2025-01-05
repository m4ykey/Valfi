package com.lyrics.data.remote.model

import androidx.annotation.Keep

@Keep
data class LyricsDtoItem(
    val albumName: String? = null,
    val duration: Double? = null,
    val id: Int? = null,
    val name: String? = null,
    val plainLyrics: String? = null,
    val trackName: String? = null,
    val artistName : String? = null
)