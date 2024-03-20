package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = false) val id : String,
    val albumId : String,
    val name : String,
    val externalUrls: String,
    val explicit : Boolean,
    val durationMs : Int,
    val artistList : String
)
