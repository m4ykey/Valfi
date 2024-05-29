package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_table",
    indices = [Index(value = ["albumType"]), Index(value = ["name"])]
)
data class AlbumEntity(
    val albumType : String,
    val artists : List<ArtistEntity>,
    val artistUrl : String = "",
    val albumUrl : String,
    @PrimaryKey(autoGenerate = false) val id : String,
    val images : String,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int,
    val saveTime : Long = System.currentTimeMillis()
)