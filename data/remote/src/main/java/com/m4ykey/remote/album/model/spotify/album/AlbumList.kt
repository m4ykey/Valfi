package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumList(
    @SerializedName("album_type")
    val albumType: String,
    @SerializedName("artists")
    val artistList: List<Artist>,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    val id: String,
    @SerializedName("images")
    val imageList: List<Image>,
    @SerializedName("name")
    val albumName: String,
    @SerializedName("total_tracks")
    val totalTracks: Int
) : Parcelable