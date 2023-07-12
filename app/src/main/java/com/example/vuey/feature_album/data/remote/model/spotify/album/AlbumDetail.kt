package com.example.vuey.feature_album.data.remote.model.spotify.album

import com.google.gson.annotations.SerializedName

data class AlbumDetail(
    @SerializedName("album_type")
    val albumType: String,
    @SerializedName("artists")
    val artistList: List<Artist>,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @SerializedName("images")
    val imageList: List<Image>,
    @SerializedName("name")
    val albumName: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("total_tracks")
    val totalTracks: Int,
    val tracks: Tracks
)