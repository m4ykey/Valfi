package com.m4ykey.data.local.model

import androidx.room.ColumnInfo

data class AlbumWithDetails(
    @ColumnInfo(name = "album_name") val albumName : String,
    @ColumnInfo(name = "album_image") val albumImage : String,
    @ColumnInfo(name = "album_total_tracks") val albumTotalTracks: Int
)
