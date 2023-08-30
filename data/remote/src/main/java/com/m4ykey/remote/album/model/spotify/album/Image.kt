package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable