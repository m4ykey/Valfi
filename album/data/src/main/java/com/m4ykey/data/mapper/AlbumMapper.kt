package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.AlbumItem
import com.m4ykey.data.domain.model.Albums
import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.ExternalUrls
import com.m4ykey.data.domain.model.Image
import com.m4ykey.data.remote.model.album.AlbumItemDto
import com.m4ykey.data.remote.model.album.AlbumsDto
import com.m4ykey.data.remote.model.album.ArtistDto
import com.m4ykey.data.remote.model.album.ExternalUrlsDto
import com.m4ykey.data.remote.model.album.ImageDto

fun ImageDto.toImage() : Image {
    return Image(
        height = height,
        url = url,
        width = width
    )
}

fun ExternalUrlsDto.toExternalUrls() : ExternalUrls {
    return ExternalUrls(spotify = spotify)
}

fun ArtistDto.toArtist() : Artist {
    return Artist(
        name = name,
        externalUrls = external_urls.toExternalUrls()
    )
}

fun AlbumsDto.toAlbums() : Albums {
    return Albums(
        limit = limit ?: 0,
        offset = offset ?: 0,
        previous = previous ?: "",
        next = next ?: "",
        items = items.map { it.toAlbumItem() }
    )
}

fun AlbumItemDto.toAlbumItem() : AlbumItem {
    return AlbumItem(
        albumType = album_type,
        artists = artists.map { it.toArtist() },
        externalUrls = external_urls.toExternalUrls(),
        id = id,
        images = images.map { it.toImage() },
        name = name,
        releaseDate = release_date,
        totalTracks = total_tracks
    )
}