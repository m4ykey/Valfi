package com.m4ykey.remote.album.model.spotify.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalUrls(
    val spotify: String
) : Parcelable