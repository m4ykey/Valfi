package com.m4ykey.common.utils

import com.m4ykey.local.movie.entity.MovieEntity
import com.m4ykey.local.movie.entity.WatchLaterEntity
import com.m4ykey.local.album.entity.AlbumEntity
import com.m4ykey.local.album.entity.ListenLaterEntity
import com.m4ykey.remote.album.model.spotify.album.AlbumList
import com.m4ykey.remote.movie.model.MovieList

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

fun MovieEntity.toMovie() : MovieList {
    return MovieList(
        id = this.movieId,
        overview = this.movieOverview,
        posterPath = this.moviePosterPath,
        releaseDate = this.movieReleaseDate,
        title = this.movieTitle,
        voteAverage = this.movieVoteAverage
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

fun WatchLaterEntity.toMovie() : MovieList {
    return MovieList(
        id = this.movieId,
        overview = "",
        posterPath = this.moviePosterPath,
        releaseDate = "",
        title = this.movieTitle,
        voteAverage = 0.0
    )
}