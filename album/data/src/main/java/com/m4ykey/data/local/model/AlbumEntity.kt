package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class AlbumEntity(
    val albumType : String,
    val artists : String,
    val artistUrl : String,
    val albumUrl : String,
    @PrimaryKey(autoGenerate = false) val id : String,
    val images : String,
    val name : String,
    val releaseDate : String,
    val totalTracks : Int,
    val saveTime : Long = System.currentTimeMillis()
)