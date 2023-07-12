package com.example.vuey.feature_album.data.remote.model.spotify.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalUrls(
    val spotify: String
) : Parcelable