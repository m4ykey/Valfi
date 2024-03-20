package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.remote.model.tracks.TrackItemDto

fun TrackItemDto.toTrackEntity(albumId : String) : TrackEntity {
    return TrackEntity(
        albumId = albumId,
        name = name,
        id = id,
        durationMs = duration_ms,
        externalUrls = external_urls.spotify,
        explicit = explicit,
        artistList = artists.joinToString(", ") { it.name }
    )
}

fun TrackEntity.toTrackItem() : TrackItem {
    return TrackItem(
        id = id,
        name = name,
        explicit = explicit,
        durationMs = durationMs,
        externalUrls = ExternalUrls(externalUrls),
        artists = artistList.split(", ").map { Artist(it, ExternalUrls(it)) }
    )
}