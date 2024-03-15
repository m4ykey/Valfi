package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.track.TrackItem
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