package com.example.vuey.core.common.utils

import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.local.entity.ListenLaterEntity
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.local.entity.WatchLaterEntity
import com.example.vuey.feature_movie.data.remote.model.MovieList

fun MovieList.toMovieEntity(): MovieEntity {
    return MovieEntity(
        movieBackdropPath = "",
        movieGenreList = emptyList(),
        movieId = this.id,
        movieOverview = this.overview,
        moviePosterPath = this.posterPath.toString(),
        movieReleaseDate = this.releaseDate,
        movieRuntime = 0,
        movieSpokenLanguage = emptyList(),
        movieTitle = this.title,
        movieVoteAverage = this.voteAverage
    )
}

fun AlbumList.toAlbumEntity(): AlbumEntity {
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

fun AlbumList.toListenLaterEntity() : ListenLaterEntity {
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
        albumImage = com.example.vuey.feature_album.data.local.entity.ListenLaterEntity.ListenLaterImage(
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
        moviePosterPath = this.posterPath.toString()
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