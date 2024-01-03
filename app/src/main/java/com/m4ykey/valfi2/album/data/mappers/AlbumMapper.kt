package com.m4ykey.valfi2.album.data.mappers

import com.m4ykey.valfi2.album.data.domain.model.Albums
import com.m4ykey.valfi2.album.data.domain.model.Artist
import com.m4ykey.valfi2.album.data.domain.model.ExternalUrls
import com.m4ykey.valfi2.album.data.domain.model.Image
import com.m4ykey.valfi2.album.data.domain.model.Item
import com.m4ykey.valfi2.album.data.remote.model.album.AlbumsDto
import com.m4ykey.valfi2.album.data.remote.model.album.ArtistDto
import com.m4ykey.valfi2.album.data.remote.model.album.ExternalUrlsDto
import com.m4ykey.valfi2.album.data.remote.model.album.ImageDto
import com.m4ykey.valfi2.album.data.remote.model.album.ItemDto

fun AlbumsDto.toAlbums() : Albums = Albums(
    items = items.map { it.toItem() },
    limit = limit,
    next = next ?: "",
    offset = offset,
    previous = previous
)

fun ItemDto.toItem() : Item {
    return Item(
        albumType = album_type,
        id = id,
        name = name,
        releaseDate = release_date,
        totalTracks = total_tracks,
        artists = artists.map { it.toArtist() },
        images = images.map { it.toImage() },
        externalUrls = external_urls.toExternalUrls()
    )
}

fun ExternalUrlsDto.toExternalUrls() : ExternalUrls = ExternalUrls(spotify = spotify)

fun ArtistDto.toArtist() : Artist {
    return Artist(
        id = id,
        name = name,
        externalUrls = external_urls.toExternalUrls()
    )
}

fun ImageDto.toImage() : Image {
    return Image(
        height = height,
        url = url,
        width = width
    )
}