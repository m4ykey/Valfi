package com.m4ykey.data.domain.model.album

import androidx.annotation.Keep

@Keep
data class Image(
    val height: Int,
    val url: String,
    val width: Int
)
