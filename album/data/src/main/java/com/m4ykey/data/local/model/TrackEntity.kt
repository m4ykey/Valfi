package com.m4ykey.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "track_table")
data class TrackEntity(
    @ColumnInfo(name = "artists") val artists : String,
    @ColumnInfo(name = "durationMs") val durationMs : Int,
    @ColumnInfo(name = "explicit") val explicit : Boolean,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "albumId") val albumId : String
) {
    val longId : Long
        get() = UUID.nameUUIDFromBytes(id.toByteArray()).mostSignificantBits
}