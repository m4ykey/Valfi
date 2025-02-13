package com.m4ykey.ui.album.helpers

import com.m4ykey.core.views.getArtistsList
import com.m4ykey.core.views.getLargestImageFromUrl
import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity

fun AlbumItem.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun AlbumDetail.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun AlbumItem.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumEntity.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun TrackItem.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumDetail.getArtistList() : String {
    return getArtistsList(
        this,
        { it.artists },
        { it.name }
    )
}