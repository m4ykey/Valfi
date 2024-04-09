package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_saved_table",
    indices = [Index(value = ["albumId"], unique = true)]
)
data class IsAlbumSaved(
    @PrimaryKey val albumId : String,
    val isAlbumSaved : Boolean
)