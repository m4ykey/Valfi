package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey(autoGenerate = false) val id : String,
    val name : String,
    val totalTracks : Int,
    val albumInfo : String,
    val image : List<ImageEntity>,
    val externalUrls : ExternalUrlsEntity,
    val artistList : List<ArtistEntity>,
    val saveTime : Long = System.currentTimeMillis()
)