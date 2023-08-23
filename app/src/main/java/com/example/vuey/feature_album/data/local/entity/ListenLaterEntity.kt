package com.example.vuey.feature_album.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.core.common.Constants.LISTEN_LATER_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = LISTEN_LATER_TABLE)
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
