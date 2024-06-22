package com.m4ykey.ui.helpers

import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.AlbumEntity

fun <T, I> getLargestImageUrl(
    item : T,
    getImageList : (T) -> List<I>,
    getHeight : (I) -> Int,
    getWidth : (I) -> Int,
    getUrl : (I) -> String
) : String? {
    return getImageList(item).maxByOrNull { getHeight(it) * getWidth(it) }?.let(getUrl)
}

fun AlbumItem.getLargestImageUrl() : String? {
    return getLargestImageUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun AlbumDetail.getLargestImageUrl() : String? {
    return getLargestImageUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}

fun <T, A> getArtistList(
    item : T,
    getArtistList : (T) -> List<A>,
    getArtistName : (A) -> String
) : String {
    return getArtistList(item).joinToString(", ") { getArtistName(it) }
}

fun AlbumItem.getArtistList() : String {
    return getArtistList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumEntity.getArtistList() : String {
    return getArtistList(
        this,
        { it.artists },
        { it.name }
    )
}

fun TrackItem.getArtistList() : String {
    return getArtistList(
        this,
        { it.artists },
        { it.name }
    )
}

fun AlbumDetail.getArtistList() : String {
    return getArtistList(
        this,
        { it.artists },
        { it.name }
    )
}