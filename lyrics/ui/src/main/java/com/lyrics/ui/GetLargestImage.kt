package com.lyrics.ui

import com.lyrics.data.domain.model.Album
import com.m4ykey.core.views.getLargestImageFromUrl

fun Album.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}