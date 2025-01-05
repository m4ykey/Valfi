package com.lyrics.data.domain.model

data class LyricsItem(
    val artistName: String,
    val duration: Double,
    val id: Int,
    val name: String,
    val plainLyrics: String,
    val trackName: String
)
