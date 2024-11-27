package com.m4ykey.ui.helpers

import com.m4ykey.core.views.getLargestImageFromUrl
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.top_tracks.Album

fun Artist.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun Album.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}