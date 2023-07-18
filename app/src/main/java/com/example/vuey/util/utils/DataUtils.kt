package com.example.vuey.util.utils

import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.feature_album.data.remote.model.spotify.album.Artist
import com.example.vuey.feature_album.data.remote.model.spotify.album.ExternalUrls
import com.example.vuey.feature_album.data.remote.model.spotify.album.Image
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.MovieList

fun MovieEntity.toMovie() : MovieList {
    return MovieList(
        id = movieId,
        overview = movieOverview,
        poster_path = moviePosterPath,
        release_date = movieReleaseDate,
        title = movieTitle,
        vote_average = movieVoteAverage
    )
}

fun MovieList.toMovieEntity() : MovieEntity {
    return MovieEntity(
        movieBackdropPath = "",
        movieGenreList = emptyList(),
        movieId = this.id,
        movieOverview = this.overview,
        moviePosterPath = this.poster_path.toString(),
        movieReleaseDate = this.release_date,
        movieRuntime = 0,
        movieSpokenLanguage = emptyList(),
        movieTitle = this.title,
        movieVoteAverage = this.vote_average
    )
}

fun AlbumEntity.toAlbum() : Album {
    return Album(
        albumType = this.albumType,
        albumName = this.albumName,
        id = this.id,
        totalTracks = this.totalTracks,
        artistList = this.artistList.map { artist ->
            Artist(
                artistName = artist.name,
                id = artist.id,
                externalUrls = ExternalUrls(
                    spotify = this.externalUrls.spotify
                )
            )
        },
        externalUrls = ExternalUrls(
            spotify = this.externalUrls.spotify
        ),
        imageList = listOf(
            Image(
            height = 640,
            width = 640,
            url = albumCover.url
        )
        )
    )
}

fun Album.toAlbumEntity() : AlbumEntity {
    return AlbumEntity(
        albumName = this.albumName,
        albumType = this.albumType,
        albumLength = 0,
        id = this.id,
        releaseDate = "",
        totalTracks = this.totalTracks,
        externalUrls = AlbumEntity.ExternalUrlsEntity(
            spotify = this.externalUrls.spotify
        ),
        albumCover = AlbumEntity.ImageEntity(
            height = 640,
            width = 640,
            url = ""
        )
    )
}