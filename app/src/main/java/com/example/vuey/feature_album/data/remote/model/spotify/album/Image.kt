package com.example.vuey.feature_album.data.remote.model.spotify.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable