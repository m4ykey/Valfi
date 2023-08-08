package com.example.vuey.feature_movie.data.local.source.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.core.common.Constants.WATCH_LATER_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = WATCH_LATER_TABLE)
data class WatchLaterEntity(
    @ColumnInfo("movieId") @PrimaryKey(autoGenerate = false) val movieId : Int,
    @ColumnInfo("movieTitle") val movieTitle : String,
    @ColumnInfo("moviePosterPath") val moviePosterPath : String,
    @ColumnInfo("saveTime") val saveTime : Long = System.currentTimeMillis()
) : Parcelable
