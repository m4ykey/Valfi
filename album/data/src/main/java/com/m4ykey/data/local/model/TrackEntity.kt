package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "track_table",
    indices = [Index(value = ["albumId"])]
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = false) val id : String,
    val albumId : String,
    val name : String,
    val externalUrls: String,
    val explicit : Boolean,
    val durationMs : Int,
    val artistList : String,
    val discNumber : Int
)