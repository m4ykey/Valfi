package com.m4ykey.data.mapper

import com.m4ykey.data.domain.model.Artist
import com.m4ykey.data.domain.model.ExternalUrls
import com.m4ykey.data.domain.model.Followers
import com.m4ykey.data.domain.model.Image
import com.m4ykey.data.domain.model.album.ArtistAlbum
import com.m4ykey.data.domain.model.top_tracks.Album
import com.m4ykey.data.domain.model.top_tracks.Track
import com.m4ykey.data.remote.model.ArtistDto
import com.m4ykey.data.remote.model.ExternalUrlsDto
import com.m4ykey.data.remote.model.FollowersDto
import com.m4ykey.data.remote.model.ImageDto
import com.m4ykey.data.remote.model.album.ArtistAlbumItemDto
import com.m4ykey.data.remote.model.top_tracks.AlbumDto
import com.m4ykey.data.remote.model.top_tracks.TrackDto

fun ImageDto.toImage() : Image =
    Image(
        height = height ?: 0,
        url = url.orEmpty(),
        width = width ?: 0
    )

fun FollowersDto.toFollowers() : Followers =
    Followers(total = total ?: 0)

fun ExternalUrlsDto.toExternalUrls() : ExternalUrls = ExternalUrls(spotify = spotify.orEmpty())

fun ArtistDto.toArtist() : Artist =
    Artist(
        id = id.orEmpty(),
        name = name.orEmpty(),
        href = href.orEmpty(),
        type = type.orEmpty(),
        uri = uri.orEmpty(),
        popularity = popularity ?: 0,
        externalUrls = external_urls?.toExternalUrls() ?: ExternalUrls(""),
        followers = followers?.toFollowers() ?: Followers(0),
        images = images?.map { it!!.toImage() }!!,
        genres = genres ?: emptyList()
    )

fun TrackDto.toTrack() : Track =
    Track(
        explicit = explicit ?: false,
        durationMs = duration_ms ?: 0,
        album = album?.toAlbum() ?: Album(emptyList(), ""),
        name = name ?: ""
    )

fun AlbumDto.toAlbum() : Album =
    Album(
        id = id ?: "",
        images = images?.map { it.toImage() }!!
    )

fun ArtistAlbumItemDto.toArtistAlbum() : ArtistAlbum =
    ArtistAlbum(
        id = id ?: "",
        name = name ?: "",
        images = images?.map { it.toImage() }!!,
        albumGroup = album_group ?: ""
    )