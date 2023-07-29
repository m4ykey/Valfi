package com.example.vuey.feature_album.data.local.source.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.core.common.Constants.ALBUM_TABLE_NAME
import com.example.vuey.core.common.Constants.ARTIST_TABLE_NAME
import com.example.vuey.core.common.Constants.TRACK_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = ALBUM_TABLE_NAME)
data class AlbumEntity(
    @ColumnInfo("trackList") val trackList: List<TrackListEntity> = emptyList(),
    @ColumnInfo("saveTime") val saveTime: Long = System.currentTimeMillis(),
    @ColumnInfo("albumType") val albumType: String,
    @ColumnInfo("releaseDate") val releaseDate: String,
    @ColumnInfo("artistList") val artistList: List<ArtistEntity> = emptyList(),
    @ColumnInfo("externalUrls") val externalUrls: ExternalUrlsEntity,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id") val id: String,
    @ColumnInfo("albumCover") val albumCover: ImageEntity,
    @ColumnInfo("albumName") val albumName: String,
    @ColumnInfo("totalTracks") val totalTracks: Int,
    @ColumnInfo("albumLength") val albumLength: Int
) : Parcelable {

    @Parcelize
    @Entity(tableName = TRACK_TABLE_NAME)
    data class TrackListEntity(
        @ColumnInfo("durationMs") val durationMs: Int,
        @ColumnInfo("albumName") val trackName: String,
        @ColumnInfo("artistList") val artistList: List<ArtistEntity> = emptyList(),
    ) : Parcelable

    @Parcelize
    @Entity(tableName = ARTIST_TABLE_NAME)
    data class ArtistEntity(
        @ColumnInfo("externalUrls")  val externalUrls: ExternalUrlsEntity,
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo("id") val id: String,
        @ColumnInfo("name") val name: String
    ) : Parcelable

    @Parcelize
    data class ExternalUrlsEntity(
        @ColumnInfo("spotify") val spotify: String
    ) : Parcelable

    @Parcelize
    data class ImageEntity(
        @ColumnInfo("height") val height: Int,
        @ColumnInfo("url") val url: String,
        @ColumnInfo("width") val width: Int
    ) : Parcelable

}