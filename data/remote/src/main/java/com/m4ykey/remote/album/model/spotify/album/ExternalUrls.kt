package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ExternalUrls(
    val spotify: String
) : Parcelable