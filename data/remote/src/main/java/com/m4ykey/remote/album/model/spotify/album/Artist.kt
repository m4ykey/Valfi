package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @SerializedName("name")
    val artistName: String,
) : Parcelable