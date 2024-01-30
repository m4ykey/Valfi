package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.album.AlbumDetail
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.domain.model.album.Artist
import com.m4ykey.data.domain.model.album.ExternalUrls
import com.m4ykey.data.domain.model.album.Image
import com.m4ykey.data.remote.model.album.AlbumDetailDto
import com.m4ykey.data.remote.model.album.AlbumItemDto
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

fun ExternalUrlsDto.toExternalUrls() : ExternalUrls = ExternalUrls(spotify = spotify)

fun ArtistDto.toArtist() : Artist = Artist(
    name = name,
    externalUrls = external_urls.toExternalUrls()
)

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

fun AlbumDetailDto.toAlbumDetail() : AlbumDetail {
    return AlbumDetail(
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