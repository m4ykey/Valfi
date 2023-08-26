package com.m4ykey.local.album.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "listen_later_table")
data class ListenLaterEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo("albumId") val albumId : String,
    @ColumnInfo("albumTitle") val albumTitle : String,
    @ColumnInfo("albumImage") val albumImage : ListenLaterImage,
    @ColumnInfo("saveTime") val saveTime : Long = System.currentTimeMillis()
) : Parcelable {

    @Parcelize
    data class ListenLaterImage(
        @ColumnInfo("height") val height: Int,
        @ColumnInfo("url") val url: String,
        @ColumnInfo("width") val width: Int
    ) : Parcelable
}
