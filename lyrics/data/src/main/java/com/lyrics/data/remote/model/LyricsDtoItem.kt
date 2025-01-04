package com.lyrics.data.remote.model

data class LyricsDtoItem(
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