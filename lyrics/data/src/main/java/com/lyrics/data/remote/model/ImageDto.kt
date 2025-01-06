package com.lyrics.data.remote.model

import androidx.annotation.Keep

@Keep
data class ImageDto(
    val height: Int? = null,
    val url: String? = null,
    val width: Int? = null
)