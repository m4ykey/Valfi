package com.m4ykey.local.album.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.ALBUM_NAME)
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
    @Entity(tableName = TableNames.TRACK_NAME)
    data class TrackListEntity(
        @ColumnInfo("durationMs") val durationMs: Int,
        @ColumnInfo("albumName") val trackName: String,
        @ColumnInfo("artistList") val artistList: List<ArtistEntity> = emptyList(),
    ) : Parcelable

    @Parcelize
    @Entity(tableName = TableNames.ARTIST_NAME)
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

object TableNames {
    const val ALBUM_NAME = "album_table"
    const val ARTIST_NAME = "artist_table"
    const val TRACK_NAME = "track_table"
}