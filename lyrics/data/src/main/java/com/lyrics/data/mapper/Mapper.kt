package com.lyrics.data.mapper

import com.lyrics.data.domain.model.Album
import com.lyrics.data.domain.model.ExternalUrls
import com.lyrics.data.domain.model.Image
import com.lyrics.data.domain.model.LyricsItem
import com.lyrics.data.domain.model.Track
import com.lyrics.data.remote.model.AlbumDto
import com.lyrics.data.remote.model.ExternalUrlsDto
import com.lyrics.data.remote.model.ImageDto
import com.lyrics.data.remote.model.LyricsDtoItem
import com.lyrics.data.remote.model.TrackDto

fun LyricsDtoItem.toLyrics() : LyricsItem {
    return LyricsItem(
        id = id ?: 0,
        name = name.orEmpty(),
        artistName = artistName.orEmpty(),
        duration = duration ?: 0.0,
        plainLyrics = plainLyrics.orEmpty(),
        trackName = trackName.orEmpty()
    )
}

fun TrackDto.toTrack() : Track {
    return Track(
        album = album?.toAlbum()!!,
        id = id.orEmpty(),
        externalUrls = external_urls?.toExternalUrls()!!
    )
}

fun ExternalUrlsDto.toExternalUrls() : ExternalUrls {
    return ExternalUrls(spotify = spotify.orEmpty())
}

fun ImageDto.toImage() : Image {
    return Image(
        url = url.orEmpty(),
        width = width ?: 0,
        height = height ?: 0
    )
}

fun AlbumDto.toAlbum() : Album {
    return Album(
        images = images?.map { it.toImage() }!!
    )
}