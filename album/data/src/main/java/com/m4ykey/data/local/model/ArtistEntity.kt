package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_table")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = false) val albumId : String,
    val name : String,
    val urls: String,
    val artistId : String
)
