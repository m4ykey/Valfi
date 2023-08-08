package com.example.vuey.core.common.utils

import com.example.vuey.feature_album.data.local.source.entity.AlbumEntity
import com.example.vuey.feature_album.data.local.source.entity.ListenLaterEntity
import com.example.vuey.feature_album.data.remote.model.spotify.album.Album
import com.example.vuey.feature_album.data.remote.model.spotify.album.Artist
import com.example.vuey.feature_album.data.remote.model.spotify.album.ExternalUrls
import com.example.vuey.feature_album.data.remote.model.spotify.album.Image
import com.example.vuey.feature_movie.data.local.source.entity.MovieEntity
import com.example.vuey.feature_movie.data.local.source.entity.WatchLaterEntity
import com.example.vuey.feature_movie.data.remote.model.MovieList

fun MovieEntity.toMovie(): MovieList {
    return MovieList(
        id = movieId,
        overview = movieOverview,
        poster_path = moviePosterPath,
        release_date = movieReleaseDate,
        title = movieTitle,
        vote_average = movieVoteAverage
    )
}

fun MovieList.toMovieEntity(): MovieEntity {
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

fun AlbumEntity.toAlbum(): Album {
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

fun Album.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        albumName = this.albumName,
        albumType = this.albumType,
        albumLength = 0,
        id = this.id,
        releaseDate = "",
        totalTracks = this.totalTracks,
        externalUrls = AlbumEntity.ExternalUrlsEntity(
            spotify = this.externalUrls!!.spotify
        ),
        albumCover = AlbumEntity.ImageEntity(
            height = 640,
            width = 640,
            url = ""
        )
    )
}

fun ListenLaterEntity.toAlbum() : Album {
    return Album(
        albumName = albumTitle,
        albumType = "",
        artistList = emptyList(),
        id = albumId,
        totalTracks = 0,
        imageList = emptyList(),
        externalUrls = null
    )
}

fun ListenLaterEntity.toAlbumEntity() : AlbumEntity {
    return AlbumEntity(
        albumName = albumTitle,
        albumType = "",
        artistList = emptyList(),
        id = albumId,
        totalTracks = 0,
        albumLength = 0,
        releaseDate = "",
        albumCover = AlbumEntity.ImageEntity(
            height = 640,
            width = 640,
            url = ""
        ),
        externalUrls = AlbumEntity.ExternalUrlsEntity(spotify = "")
    )
}

fun Album.toListenLaterEntity() : ListenLaterEntity {
    return ListenLaterEntity(
        albumId = this.id,
        albumTitle = this.albumName,
        albumImage = ListenLaterEntity.ListenLaterImage(
            url = "",
            width = 0,
            height = 0
        )
    )
}

fun AlbumEntity.toListenLaterEntity() : ListenLaterEntity {
    return ListenLaterEntity(
        albumId = this.id,
        albumTitle = this.albumName,
        albumImage = ListenLaterEntity.ListenLaterImage(
            url = "",
            width = 0,
            height = 0
        )
    )
}

fun MovieEntity.toWatchLaterEntity() : WatchLaterEntity {
    return WatchLaterEntity(
        movieId = this.movieId,
        moviePosterPath = this.moviePosterPath,
        movieTitle = this.movieTitle
    )
}

fun MovieList.toWatchLaterEntity() : WatchLaterEntity {
    return WatchLaterEntity(
        movieTitle = this.title,
        movieId = this.id,
        moviePosterPath = this.poster_path.toString()
    )
}

fun WatchLaterEntity.toMovie() : MovieList {
    return MovieList(
        id = this.movieId,
        overview = "",
        poster_path = this.moviePosterPath,
        release_date = "",
        title = this.movieTitle,
        vote_average = 0.0
    )
}

fun WatchLaterEntity.toMovieEntity() : MovieEntity {
    return MovieEntity(
        movieBackdropPath = "",
        movieId = this.movieId,
        moviePosterPath = this.moviePosterPath,
        movieTitle = this.movieTitle,
        movieGenreList = emptyList(),
        movieOverview = "",
        movieReleaseDate = "",
        movieRuntime = 0,
        movieSpokenLanguage = emptyList(),
        movieVoteAverage = 0.0
    )
}