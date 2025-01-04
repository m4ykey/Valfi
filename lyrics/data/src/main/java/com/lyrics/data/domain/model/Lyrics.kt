package com.lyrics.data.domain.model

data class Lyrics(
    val albumName: String,
    val artistName: String,
    val duration: Double,
    val id: Int,
    val instrumental: Boolean,
    val name: String,
    val plainLyrics: String,
    val syncedLyrics: String,
    val trackName: String
)
