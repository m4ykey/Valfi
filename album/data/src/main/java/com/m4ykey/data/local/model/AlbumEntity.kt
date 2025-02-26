package com.m4ykey.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_table",
    indices = [Index(value = ["album_type", "name"], unique = true)]
)
data class AlbumEntity(
    @ColumnInfo(name = "album_type") val albumType : String,
    @ColumnInfo(name = "artists") val artists : List<ArtistEntity>,
    @ColumnInfo(name = "album_url") val albumUrl : String,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id : String,
    @ColumnInfo(name = "images") val images : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "release_date") val releaseDate : String,
    @ColumnInfo(name = "total_tracks") val totalTracks : Int,
    @ColumnInfo(name = "save_time") val saveTime : Long? = null
)