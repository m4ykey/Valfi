package com.m4ykey.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_table")
data class ArtistEntity(
    @ColumnInfo(name = "album_id") @PrimaryKey(autoGenerate = false) val albumId : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "artist_id") val artistId : String
)
