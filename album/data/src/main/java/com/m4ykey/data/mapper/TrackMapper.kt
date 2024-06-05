package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls
import com.m4ykey.data.domain.model.track.TrackItem
import com.m4ykey.data.remote.model.tracks.TrackItemDto

fun TrackItemDto.toTrackItem() : TrackItem =
    TrackItem(
        id = id.orEmpty(),
        discNumber = disc_number ?: 0,
        durationMs = duration_ms ?: 0,
        explicit = explicit == true,
        name = name.orEmpty(),
        artists = artists!!.map { it.toArtist() },
        externalUrls = external_urls!!.toExternalUrls()
    )