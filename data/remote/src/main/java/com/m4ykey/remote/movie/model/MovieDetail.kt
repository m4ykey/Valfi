package com.m4ykey.remote.movie.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetail(
    @field:Json(name = "backdrop_path")
    val backdropPath: String?,
    @field:Json(name = "genres")
    val genreList: List<Genre>,
    val id: Int,
    val overview: String,
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "release_date")
    val releaseDate: String,
    val runtime: Int,
    @field:Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    @field:Json(name = "vote_average")
    val voteAverage: Double,
)