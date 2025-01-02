package com.m4ykey.data.domain.repository

interface LyricsRepository {

    suspend fun searchLyrics(artist : String, song : String) : String
    suspend fun getSongLyrics(songUrl : String) : String

}