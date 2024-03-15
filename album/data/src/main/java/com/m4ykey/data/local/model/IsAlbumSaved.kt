package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_saved_table")
data class IsAlbumSaved(
    @PrimaryKey val albumId : String,
    val isAlbumSaved : Boolean
)