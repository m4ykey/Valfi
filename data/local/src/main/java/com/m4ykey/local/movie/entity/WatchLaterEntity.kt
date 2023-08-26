package com.m4ykey.local.movie.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "watch_later_table")
data class WatchLaterEntity(
    @ColumnInfo("movieId") @PrimaryKey(autoGenerate = false) val movieId : Int,
    @ColumnInfo("movieTitle") val movieTitle : String,
    @ColumnInfo("moviePosterPath") val moviePosterPath : String,
    @ColumnInfo("saveTime") val saveTime : Long = System.currentTimeMillis()
) : Parcelable
