package com.m4ykey.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls

@Entity(tableName = "album_tracks")
data class TrackEntity(
    val artists: List<Artist>,
    val durationMs: Int,
    val explicit: Boolean,
    val externalUrls: ExternalUrls,
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val albumId : String
)
