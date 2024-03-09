package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.local.model.TrackEntity
import com.m4ykey.data.remote.model.tracks.TrackItemDto

fun TrackItemDto.toTrackItem() : TrackItem {
    return TrackItem(
        durationMs = duration_ms,
        id = id,
        name = name,
        explicit = explicit,
        externalUrls = external_urls.toExternalUrls(),
        artists = artists.map { it.toArtist() }
    )
}

fun TrackItemDto.toTrackEntity(albumId : String) : TrackEntity {
    return TrackEntity(
        id = id,
        name = name,
        durationMs = duration_ms,
        explicit = explicit,
        externalUrls = external_urls.toExternalUrls(),
        artists = artists.map { it.toArtist() },
        albumId = albumId
    )
}

fun TrackEntity.toTrackItem() : TrackItem {
    return TrackItem(
        durationMs = durationMs,
        artists = artists,
        explicit = explicit,
        externalUrls = externalUrls,
        id = id,
        name = name
    )
}