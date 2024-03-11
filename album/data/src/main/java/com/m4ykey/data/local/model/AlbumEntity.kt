package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val totalTracks: Int,
    val albumUrl: String,
    val artistUrl: String,
    val image: String,
    val artistList: String,
    val saveTime: Long = System.currentTimeMillis(),
    val albumType: String,
    val releaseDate: String,
    var isAlbumSaved: Boolean,
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val year : Int = Calendar.getInstance().get(Calendar.YEAR),
    val color : Int? = null
)